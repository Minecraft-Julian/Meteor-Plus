package com.meteorplus.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.BlockRegistry;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.ScriptExecutor;
import com.meteorplus.scripting.Variable;
import com.meteorplus.scripting.storage.ScriptData;
import com.meteorplus.scripting.storage.ScriptManager;

import net.minecraft.client.MinecraftClient;

/**
 * Script manager for MeteorPlus.
 *
 * This class is intentionally kept independent from Meteor Client internals so
 * the core scripting engine can be developed and tested separately.
 */
public class ScriptBuilder {
    private final ScriptManager scriptManager;
    private final ScriptExecutor executor;
    private ScriptData activeScript;
    private boolean loopEnabled;
    private boolean autoRun;

    public ScriptBuilder(MinecraftClient client) {
        this.scriptManager = new ScriptManager(client);
        this.executor = new ScriptExecutor(client);
    }

    public List<String> getAvailableScripts() {
        return scriptManager.getScriptNames();
    }

    public void selectScript(String scriptName) {
        this.activeScript = scriptManager.loadScript(scriptName);
    }

    public ScriptData getActiveScript() {
        return activeScript;
    }

    public void setLoopEnabled(boolean loopEnabled) {
        this.loopEnabled = loopEnabled;
    }

    public void setAutoRun(boolean autoRun) {
        this.autoRun = autoRun;
    }

    public boolean isLoopEnabled() {
        return loopEnabled;
    }

    public boolean isAutoRun() {
        return autoRun;
    }

    public void createScript(String name, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Script name must not be empty");
        }
        ScriptData script = new ScriptData(name.trim(), description == null ? "" : description);
        scriptManager.saveScript(script);
        this.activeScript = script;
    }

    public void saveScript(ScriptData scriptData) {
        if (scriptData == null) {
            return;
        }
        scriptManager.saveScript(scriptData);
    }

    public void deleteScript(String scriptName) {
        if (scriptName == null) {
            return;
        }
        scriptManager.deleteScript(scriptName);
        if (activeScript != null && scriptName.equals(activeScript.getName())) {
            activeScript = null;
        }
    }

    public void importScript(java.nio.file.Path sourcePath) {
        scriptManager.importScript(sourcePath);
    }

    public void exportScript(String scriptName, java.nio.file.Path destination) {
        scriptManager.exportScript(scriptName, destination);
    }

    public void startScript() {
        if (activeScript == null) {
            return;
        }

        Block firstBlock = buildBlockChain(activeScript);
        if (firstBlock == null) {
            return;
        }

        executor.executeScript(activeScript.getName(), firstBlock, new ScriptExecutor.ScriptExecutionListener() {
            @Override
            public void onScriptStart(String scriptName) {
                System.out.println("Script started: " + scriptName);
            }

            @Override
            public void onScriptComplete(String scriptName) {
                System.out.println("Script completed: " + scriptName);
                if (loopEnabled && !executor.isRunning()) {
                    startScript();
                }
            }

            @Override
            public void onScriptError(String scriptName, Exception error) {
                error.printStackTrace();
            }

            @Override
            public void onScriptStop(String scriptName) {
                System.out.println("Script stopped: " + scriptName);
            }
        });
    }

    public void stopScript() {
        executor.stopScript();
    }

    public boolean isScriptRunning() {
        return executor.isRunning();
    }

    private Block buildBlockChain(ScriptData scriptData) {
        List<ScriptData.BlockEntry> entries = scriptData.getBlocks();
        if (entries == null || entries.isEmpty()) {
            return null;
        }

        List<Block> blockInstances = new ArrayList<>();
        for (ScriptData.BlockEntry entry : entries) {
            Block block = BlockRegistry.createBlock(entry.getId());
            if (block == null) {
                continue;
            }
            block.setParameters(entry.getParams());
            blockInstances.add(block);
        }

        for (int i = 0; i < blockInstances.size(); i++) {
            ScriptData.BlockEntry entry = entries.get(i);
            Block block = blockInstances.get(i);

            if (entry.getNextBlockIndex() >= 0 && entry.getNextBlockIndex() < blockInstances.size()) {
                block.setNextBlock(blockInstances.get(entry.getNextBlockIndex()));
            }

            if (entry.getChildBlockIndices() != null && !entry.getChildBlockIndices().isEmpty()) {
                for (Integer childIndex : entry.getChildBlockIndices()) {
                    if (childIndex >= 0 && childIndex < blockInstances.size()) {
                        block.addChildBlock(blockInstances.get(childIndex));
                    }
                }
            }
        }

        return blockInstances.get(0);
    }
}
