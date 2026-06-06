package com.meteorplus.scripting.storage;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.Variable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a script as data that can be serialized to JSON.
 * Format: .meteorplus file (which is JavaScript text with embedded JSON)
 */
public class ScriptData {
    public String name;
    public String description;
    public long createdAt;
    public long lastModified;
    public List<BlockData> blocks;
    public Map<String, VariableData> variables;

    public static class BlockData {
        public String id;                          // Block type ID
        public Map<String, VariableData> params;  // Block parameters
        public int nextBlockIndex;                 // Index of next block (-1 if none)
        public List<BlockData> children;           // For container blocks

        public BlockData() {
            this.nextBlockIndex = -1;
            this.children = new ArrayList<>();
        }
    }

    public static class VariableData {
        public String name;
        public Object value;
        public String type;  // VariableType enum as string

        public VariableData() {}

        public VariableData(String name, Object value, String type) {
            this.name = name;
            this.value = value;
            this.type = type;
        }
    }

    public ScriptData() {
        this.blocks = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.lastModified = System.currentTimeMillis();
    }

    public ScriptData(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    /**
     * Wraps JSON data in JavaScript comment format for .meteorplus files
     */
    public String toMeteorPlusFormat() {
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("description", description);
        json.addProperty("createdAt", createdAt);
        json.addProperty("lastModified", lastModified);
        
        String jsonStr = json.toString();
        return "/*\n * MeteorPlus Script: " + name + "\n */\nconst scriptData = " + jsonStr + ";\n";
    }
}
