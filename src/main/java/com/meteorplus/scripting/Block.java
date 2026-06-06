package com.meteorplus.scripting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for all script blocks.
 */
public abstract class Block {
    private final String id;
    private final String displayName;
    private final String category;
    private final String color;
    private Block nextBlock;
    private final Map<String, Object> parameters = new HashMap<>();
    private final List<Block> childBlocks = new ArrayList<>();

    public Block(String id, String displayName, String category, String color) {
        this.id = id;
        this.displayName = displayName;
        this.category = category;
        this.color = color;
    }

    public abstract void execute(ExecutionContext ctx) throws Exception;

    public abstract String[] getInputSlots();

    public abstract Variable.VariableType getInputSlotType(String slotName);

    public void executeNext(ExecutionContext ctx) throws Exception {
        if (ctx.isStopped()) {
            return;
        }

        if (nextBlock != null) {
            nextBlock.execute(ctx);
        }
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCategory() {
        return category;
    }

    public String getColor() {
        return color;
    }

    public Block getNextBlock() {
        return nextBlock;
    }

    public void setNextBlock(Block nextBlock) {
        this.nextBlock = nextBlock;
    }

    public void setParameter(String slotName, Object value) {
        parameters.put(slotName, value);
    }

    public Object getParameter(String slotName) {
        return parameters.get(slotName);
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> params) {
        parameters.clear();
        if (params != null) {
            parameters.putAll(params);
        }
    }

    public String getParameterAsString(String slotName) {
        Object value = getParameter(slotName);
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    public double getParameterAsNumber(String slotName) {
        Object value = getParameter(slotName);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean getParameterAsBoolean(String slotName) {
        Object value = getParameter(slotName);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue() != 0;
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }

    public void addChildBlock(Block block) {
        if (block != null) {
            childBlocks.add(block);
        }
    }

    public List<Block> getChildBlocks() {
        return childBlocks;
    }
}
