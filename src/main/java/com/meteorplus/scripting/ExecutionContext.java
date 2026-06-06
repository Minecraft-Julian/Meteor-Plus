package com.meteorplus.scripting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;
import java.util.HashMap;
import java.util.Map;

/**
 * Context for script execution.
 * Provides access to player, world, and variable storage during script runtime.
 * Supports Minecraft-specific actions like movement, rotation, and block interaction.
 */
public class ExecutionContext {
    private MinecraftClient client;
    private ClientPlayerEntity player;
    private Map<String, Variable> variables;
    private boolean running;
    private boolean paused;
    private Thread executionThread;

    public ExecutionContext(MinecraftClient client) {
        this.client = client;
        this.player = client.player;
        this.variables = new HashMap<>();
        this.running = true;
        this.paused = false;
    }

    // Getters
    public MinecraftClient getClient() {
        return client;
    }

    public ClientPlayerEntity getPlayer() {
        return player;
    }

    public Map<String, Variable> getVariables() {
        return variables;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPaused() {
        return paused;
    }

    // Variable management
    public void setVariable(String name, Variable variable) {
        variables.put(name, variable);
    }

    public Variable getVariable(String name) {
        return variables.getOrDefault(name, null);
    }

    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }

    public void removeVariable(String name) {
        variables.remove(name);
    }

    public void clearVariables() {
        variables.clear();
    }

    // Execution control
    public void stop() {
        this.running = false;
    }

    public void pause() {
        this.paused = true;
    }

    public void resume() {
        this.paused = false;
    }

    public void setExecutionThread(Thread thread) {
        this.executionThread = thread;
    }

    public Thread getExecutionThread() {
        return executionThread;
    }

    /**
     * Executes a Minecraft command
     */
    public void executeCommand(String command) {
        if (player == null || !player.isAlive()) {
            return;
        }

        // Remove leading slash if present
        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        // Send command to server
        player.networkHandler.sendCommand(command);
    }

    /**
     * Sends a chat message (not as command)
     */
    public void sendChat(String message) {
        if (player == null) {
            return;
        }
        player.networkHandler.sendChatMessage(message);
    }

    /**
     * Waits for specified milliseconds (with check for pause/stop)
     */
    public void wait(long milliseconds) throws InterruptedException {
        long endTime = System.currentTimeMillis() + milliseconds;
        while (System.currentTimeMillis() < endTime) {
            if (!running) {
                throw new InterruptedException("Script stopped");
            }
            if (paused) {
                Thread.sleep(100); // Check pause status every 100ms
            } else {
                Thread.sleep(50);
            }
        }
    }

    /**
     * Waits for specified ticks (1 tick = 50ms in Minecraft)
     */
    public void waitTicks(long ticks) throws InterruptedException {
        wait(ticks * 50);
    }

    /**
     * Waits for specified seconds
     */
    public void waitSeconds(double seconds) throws InterruptedException {
        wait((long) (seconds * 1000));
    }

    /**
     * Checks if the player is still valid
     */
    public boolean isPlayerValid() {
        return player != null && player.isAlive();
    }

    // ===== MINECRAFT-SPECIFIC METHODS =====

    /**
     * Moves player in specified direction for specified distance
     * Direction: "forward", "backward", "left", "right"
     */
    public void executeMovement(String direction, double distance) throws InterruptedException {
        if (!isPlayerValid()) {
            return;
        }

        // Convert distance to ticks needed
        long ticksNeeded = (long) (distance / 0.1); // ~0.1 blocks per tick when walking
        
        // For each tick, simulate holding down the movement key
        for (long i = 0; i < ticksNeeded && running; i++) {
            // Send movement input via key press simulation
            switch (direction.toLowerCase()) {
                case "forward":
                    player.input.movementForward = 1.0f;
                    break;
                case "backward":
                    player.input.movementForward = -1.0f;
                    break;
                case "left":
                    player.input.movementSideways = -1.0f;
                    break;
                case "right":
                    player.input.movementSideways = 1.0f;
                    break;
            }
            
            waitTicks(1);
        }
        
        // Stop movement
        player.input.movementForward = 0;
        player.input.movementSideways = 0;
    }

    /**
     * Rotates player view
     * yaw: horizontal rotation (positive = left, negative = right)
     * pitch: vertical rotation (positive = up, negative = down)
     */
    public void rotatePlayer(float yaw, float pitch) {
        if (!isPlayerValid()) {
            return;
        }

        player.setYaw(player.getYaw() + yaw);
        player.setPitch(MathHelper.clamp(player.getPitch() + pitch, -90f, 90f));
    }

    /**
     * Makes player look at specific coordinates
     */
    public void lookAtCoordinates(double x, double y, double z) {
        if (!isPlayerValid()) {
            return;
        }

        // Calculate direction from player to target
        Vec3d playerPos = player.getCameraPosVec(1.0f);
        Vec3d targetPos = new Vec3d(x, y, z);
        Vec3d direction = targetPos.subtract(playerPos);

        // Calculate yaw and pitch
        double yaw = Math.atan2(direction.z, direction.x) * 180 / Math.PI - 90;
        double pitch = Math.atan2(direction.y, Math.sqrt(direction.x * direction.x + direction.z * direction.z)) * 180 / Math.PI;

        player.setYaw((float) yaw);
        player.setPitch((float) -pitch);
    }

    /**
     * Checks if player is touching specified block type
     */
    public boolean isPlayerTouchingBlock(String blockName) {
        if (!isPlayerValid() || client.world == null) {
            return false;
        }

        // Check blocks around player
        int playerX = (int) player.getX();
        int playerY = (int) player.getY();
        int playerZ = (int) player.getZ();

        // Check nearby blocks (3x3x3 cube around player)
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 2; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    net.minecraft.block.Block block = client.world.getBlockState(
                        new net.minecraft.util.math.BlockPos(playerX + dx, playerY + dy, playerZ + dz)
                    ).getBlock();
                    
                    if (block.getName().getString().toLowerCase().contains(blockName.toLowerCase())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Checks if a key is currently pressed
     */
    public boolean isKeyPressed(String key) {
        // Map key names to input states
        switch (key.toLowerCase()) {
            case "w":
            case "forward":
                return player.input.movementForward > 0;
            case "a":
            case "left":
                return player.input.movementSideways < 0;
            case "s":
            case "backward":
                return player.input.movementForward < 0;
            case "d":
            case "right":
                return player.input.movementSideways > 0;
            case "space":
            case "jump":
                return player.input.jumping;
            case "shift":
            case "sneak":
                return player.input.sneaking;
            default:
                return false;
        }
    }

    /**
     * Makes player interact with block (left-click/break)
     */
    public void attackBlock() {
        if (!isPlayerValid()) {
            return;
        }
        
        // Simulate left-click
        if (client.interactionManager != null) {
            var hitResult = client.crosshairTarget;
            if (hitResult instanceof net.minecraft.util.hit.BlockHitResult) {
                net.minecraft.util.hit.BlockHitResult blockHit = (net.minecraft.util.hit.BlockHitResult) hitResult;
                client.interactionManager.attackBlock(blockHit.getBlockPos(), blockHit.getSide());
            }
        }
    }

    /**
     * Makes player use block (right-click/place)
     */
    public void useBlock() {
        if (!isPlayerValid()) {
            return;
        }
        
        // Simulate right-click
        if (client.interactionManager != null) {
            var hitResult = client.crosshairTarget;
            if (hitResult instanceof net.minecraft.util.hit.BlockHitResult) {
                net.minecraft.util.hit.BlockHitResult blockHit = (net.minecraft.util.hit.BlockHitResult) hitResult;
                client.interactionManager.interactBlock(player, client.world, net.minecraft.util.Hand.MAIN_HAND, blockHit);
            }
        }
    }

    /**
     * Checks if player is in survival mode (no cheats)
     */
    public boolean isSurvivalMode() {
        if (client.world == null) {
            return false;
        }
        return client.world.isClient && 
               client.interactionManager != null && 
               client.interactionManager.getCurrentGameMode() == GameMode.SURVIVAL;
    }

    /**
     * Gets player's current position
     */
    public Vec3d getPlayerPosition() {
        if (!isPlayerValid()) {
            return Vec3d.ZERO;
        }
        return player.getPos();
    }

    /**
     * Gets block player is looking at
     */
    public net.minecraft.util.math.BlockPos getLookingAtBlock() {
        if (client.crosshairTarget instanceof net.minecraft.util.hit.BlockHitResult) {
            net.minecraft.util.hit.BlockHitResult blockHit = (net.minecraft.util.hit.BlockHitResult) client.crosshairTarget;
            return blockHit.getBlockPos();
        }
        return null;
    }
}
