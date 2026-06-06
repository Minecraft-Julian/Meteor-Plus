package com.meteorplus.scripting;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;

/**
 * Execution environment for running scripts.
 */
public class ExecutionContext {
    private final MinecraftClient client;
    private final Map<String, Variable> variables = new HashMap<>();
    private volatile boolean paused;
    private volatile boolean stopped;
    private Thread executionThread;

    public ExecutionContext(MinecraftClient client) {
        this.client = client;
        this.paused = false;
        this.stopped = false;
    }

    public MinecraftClient getClient() {
        return client;
    }

    public void setExecutionThread(Thread executionThread) {
        this.executionThread = executionThread;
    }

    public void stop() {
        stopped = true;
        if (executionThread != null) {
            executionThread.interrupt();
        }
    }

    public boolean isStopped() {
        return stopped;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
        synchronized (this) {
            notifyAll();
        }
    }

    public void waitSeconds(double seconds) throws InterruptedException {
        if (seconds <= 0 || stopped) {
            return;
        }

        long remaining = (long) (seconds * 1000);
        long endTime = System.currentTimeMillis() + remaining;

        while (!stopped && System.currentTimeMillis() < endTime) {
            synchronized (this) {
                while (paused && !stopped) {
                    wait(50);
                }
            }
            if (stopped) {
                return;
            }
            long sleepTime = Math.min(50, endTime - System.currentTimeMillis());
            if (sleepTime > 0) {
                Thread.sleep(sleepTime);
            }
        }
    }

    public void waitTicks(int ticks) throws InterruptedException {
        if (ticks <= 0 || stopped) {
            return;
        }

        for (int i = 0; i < ticks && !stopped; i++) {
            synchronized (this) {
                while (paused && !stopped) {
                    wait(50);
                }
            }
            if (stopped) {
                return;
            }
            Thread.sleep(50);
        }
    }

    public void setVariable(String name, Variable variable) {
        if (name == null || variable == null) {
            return;
        }
        variables.put(name, variable);
    }

    public Variable getVariable(String name) {
        return variables.get(name);
    }

    public void clearVariables() {
        variables.clear();
    }

    public void sendChat(String message) {
        if (client == null || client.player == null || message == null) {
            return;
        }
        client.player.sendChatMessage(message, false);
    }

    public void executeCommand(String command) {
        if (command == null || command.isBlank()) {
            return;
        }
        if (command.startsWith("/")) {
            sendChat(command);
        } else {
            sendChat("/" + command);
        }
    }

    public void moveForward(double distance) {
        if (client == null || client.player == null) {
            return;
        }

        Vec3d direction = client.player.getRotationVector().multiply(distance);
        client.player.setVelocity(direction);
    }

    public void turn(double degrees) {
        if (client == null || client.player == null) {
            return;
        }

        float newYaw = client.player.getYaw() + (float) degrees;
        client.player.setYaw(newYaw);
        client.player.setHeadYaw(newYaw);
    }

    public void lookAtCoordinates(double x, double y, double z) {
        if (client == null || client.player == null) {
            return;
        }

        Vec3d position = client.player.getPos();
        double dx = x - position.x;
        double dy = y - (position.y + client.player.getEyeHeight(client.getTickDelta()));
        double dz = z - position.z;
        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        double yaw = Math.toDegrees(Math.atan2(-dx, dz));
        double pitch = Math.toDegrees(-Math.atan2(dy, distanceXZ));

        client.player.setYaw((float) yaw);
        client.player.setPitch((float) pitch);
        client.player.setHeadYaw((float) yaw);
    }

    public boolean isKeyPressed(String key) {
        if (client == null || client.options == null) {
            return false;
        }

        key = key == null ? "" : key.toLowerCase();
        KeyBinding binding;

        switch (key) {
            case "w":
            case "forward":
                binding = client.options.keyForward;
                break;
            case "s":
            case "back":
                binding = client.options.keyBack;
                break;
            case "a":
            case "left":
                binding = client.options.keyLeft;
                break;
            case "d":
            case "right":
                binding = client.options.keyRight;
                break;
            case "space":
                binding = client.options.keyJump;
                break;
            case "shift":
            case "sneak":
                binding = client.options.keySneak;
                break;
            default:
                return false;
        }

        return binding.isPressed();
    }

    public boolean isPlayerTouchingBlock(String blockName) {
        if (client == null || client.player == null || client.world == null || blockName == null) {
            return false;
        }

        BlockPos playerPos = client.player.getBlockPos();
        String normalized = blockName.toLowerCase();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos checkPos = playerPos.add(x, y, z);
                    var state = client.world.getBlockState(checkPos);
                    String stateName = state.getBlock().getTranslationKey().toLowerCase();
                    if (stateName.contains(normalized)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
