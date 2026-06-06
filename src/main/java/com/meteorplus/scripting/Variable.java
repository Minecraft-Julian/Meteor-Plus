package com.meteorplus.scripting;

/**
 * Represents a variable that can be used in scripts.
 * Variables can be dragged into block parameters like in Scratch.
 */
public class Variable {
    private String name;
    private Object value;
    private VariableType type;

    public enum VariableType {
        ITEM,          // Minecraft item name
        NUMBER,        // Integer or Double
        TEXT,          // String
        PLAYER,        // Player name or UUID
        POSITION,      // X, Y, Z coordinates
        ROTATION,      // Pitch, Yaw
        COMMAND,       // Custom command
        BOOLEAN        // True/False
    }

    public Variable(String name, Object value, VariableType type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    // Copy constructor
    public Variable(Variable other) {
        this.name = other.name;
        this.value = other.value;
        this.type = other.type;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public VariableType getType() {
        return type;
    }

    // Setters
    public void setValue(Object value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%s)", name, value, type);
    }

    /**
     * Gets the value as a string for commands
     */
    public String getAsString() {
        if (value == null) return "";
        return value.toString();
    }

    /**
     * Gets the value as a number
     */
    public double getAsNumber() {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
