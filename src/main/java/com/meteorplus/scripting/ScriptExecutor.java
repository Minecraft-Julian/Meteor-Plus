package com.meteorplus.scripting;

import net.minecraft.client.MinecraftClient;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

/**
 * Executes scripts by running blocks sequentially.
 * Handles threading, error handling, and execution control.
 */
public class ScriptExecutor {
    private static final Logger LOG = LogUtils.getLogger();

    private ExecutionContext context;
    private Thread executionThread;
    private volatile boolean running;
    private ScriptExecutionListener listener;

    public interface ScriptExecutionListener {
        void onScriptStart(String scriptName);
        void onScriptComplete(String scriptName);
        void onScriptError(String scriptName, Exception error);
        void onScriptStop(String scriptName);
    }

    public ScriptExecutor(MinecraftClient client) {
        this.context = new ExecutionContext(client);
        this.running = false;
    }

    /**
     * Starts executing a script
     */
    public void executeScript(String scriptName, Block firstBlock, ScriptExecutionListener listener) {
        if (running) {
            LOG.warn("Script is already running");
            return;
        }

        this.listener = listener;
        this.running = true;

        executionThread = new Thread(() -> {
            try {
                if (listener != null) {
                    listener.onScriptStart(scriptName);
                }

                LOG.info("Starting script: {}", scriptName);
                firstBlock.execute(context);

                if (running && listener != null) {
                    listener.onScriptComplete(scriptName);
                }
                LOG.info("Script completed: {}", scriptName);

            } catch (InterruptedException e) {
                if (listener != null) {
                    listener.onScriptStop(scriptName);
                }
                LOG.info("Script stopped: {}", scriptName);
                Thread.currentThread().interrupt();

            } catch (Exception e) {
                LOG.error("Script error: {}", scriptName, e);
                if (listener != null) {
                    listener.onScriptError(scriptName, e);
                }

            } finally {
                running = false;
            }
        }, "ScriptExecutor-" + scriptName);

        context.setExecutionThread(executionThread);
        executionThread.start();
    }

    /**
     * Stops the currently running script
     */
    public void stopScript() {
        if (!running) {
            return;
        }

        running = false;
        context.stop();

        if (executionThread != null) {
            executionThread.interrupt();
            try {
                executionThread.join(5000); // Wait up to 5 seconds for thread to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Pauses the currently running script
     */
    public void pauseScript() {
        if (!running) {
            return;
        }
        context.pause();
    }

    /**
     * Resumes the paused script
     */
    public void resumeScript() {
        if (!running) {
            return;
        }
        context.resume();
    }

    /**
     * Checks if a script is currently running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Gets the execution context
     */
    public ExecutionContext getContext() {
        return context;
    }

    /**
     * Sets a variable in the script context
     */
    public void setVariable(String name, Variable variable) {
        context.setVariable(name, variable);
    }

    /**
     * Gets a variable from the script context
     */
    public Variable getVariable(String name) {
        return context.getVariable(name);
    }

    /**
     * Clears all variables
     */
    public void clearVariables() {
        context.clearVariables();
    }
}
