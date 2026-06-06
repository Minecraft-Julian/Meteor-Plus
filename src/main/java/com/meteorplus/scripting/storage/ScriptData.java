package com.meteorplus.scripting.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Data structure used for script serialization and deserialization.
 */
public class ScriptData {
    private String name;
    private String description;
    private long createdAt;
    private long lastModified;
    private List<BlockEntry> blocks = new ArrayList<>();

    public ScriptData() {
        // required for GSON
    }

    public ScriptData(String name, String description) {
        this.name = name;
        this.description = description;
        this.createdAt = System.currentTimeMillis();
        this.lastModified = this.createdAt;
        this.blocks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public List<BlockEntry> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BlockEntry> blocks) {
        this.blocks = blocks;
    }

    public void addBlock(BlockEntry blockEntry) {
        this.blocks.add(blockEntry);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public static ScriptData fromJson(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, ScriptData.class);
    }

    public static class BlockEntry {
        private String id;
        private Map<String, Object> params = new HashMap<>();
        private int nextBlockIndex = -1;
        private List<Integer> childBlockIndices = new ArrayList<>();

        public BlockEntry() {
            // required for GSON
        }

        public BlockEntry(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Map<String, Object> getParams() {
            return params;
        }

        public void setParams(Map<String, Object> params) {
            this.params = params;
        }

        public int getNextBlockIndex() {
            return nextBlockIndex;
        }

        public void setNextBlockIndex(int nextBlockIndex) {
            this.nextBlockIndex = nextBlockIndex;
        }

        public List<Integer> getChildBlockIndices() {
            return childBlockIndices;
        }

        public void setChildBlockIndices(List<Integer> childBlockIndices) {
            this.childBlockIndices = childBlockIndices;
        }
    }
}
