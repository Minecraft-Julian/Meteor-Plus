package com.meteorplus.scripting.storage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages saving and loading MeteorPlus scripts.
 * Stores scripts in .meteorplus files (which are JavaScript with embedded JSON).
 */
public class ScriptManager {
    private static final Logger LOG = LogUtils.getLogger();
    private static final String SCRIPTS_DIR = "meteorplus/scripts";
    private static final String FILE_EXTENSION = ".meteorplus";

    static {
        try {
            Files.createDirectories(Paths.get(SCRIPTS_DIR));
        } catch (IOException e) {
            LOG.error("Failed to create scripts directory", e);
        }
    }

    /**
     * Saves a script to a .meteorplus file
     */
    public static void saveScript(ScriptData script) throws IOException {
        String filename = sanitizeFilename(script.name) + FILE_EXTENSION;
        Path path = Paths.get(SCRIPTS_DIR, filename);

        String content = script.toMeteorPlusFormat();
        Files.write(path, content.getBytes());

        LOG.info("Script saved: {} ({})", script.name, path);
    }

    /**
     * Loads a script from a .meteorplus file
     */
    public static ScriptData loadScript(String scriptName) throws IOException {
        String filename = sanitizeFilename(scriptName) + FILE_EXTENSION;
        Path path = Paths.get(SCRIPTS_DIR, filename);

        if (!Files.exists(path)) {
            throw new FileNotFoundException("Script not found: " + scriptName);
        }

        String content = new String(Files.readAllBytes(path));
        ScriptData script = parseScriptContent(content);

        LOG.info("Script loaded: {} from {}", scriptName, path);
        return script;
    }

    /**
     * Parses script content from .meteorplus file format
     */
    private static ScriptData parseScriptContent(String content) throws IOException {
        try {
            int jsonStart = content.indexOf('{');
            int jsonEnd = content.lastIndexOf('}') + 1;

            if (jsonStart == -1 || jsonEnd <= jsonStart) {
                throw new IOException("Invalid script format");
            }

            String jsonStr = content.substring(jsonStart, jsonEnd);
            JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
            
            ScriptData script = new ScriptData();
            script.name = json.get("name").getAsString();
            script.description = json.has("description") ? json.get("description").getAsString() : "";
            script.createdAt = json.has("createdAt") ? json.get("createdAt").getAsLong() : System.currentTimeMillis();
            script.lastModified = json.has("lastModified") ? json.get("lastModified").getAsLong() : System.currentTimeMillis();
            
            return script;
        } catch (Exception e) {
            throw new IOException("Failed to parse script", e);
        }
    }

    /**
     * Lists all available scripts
     */
    public static List<String> listScripts() {
        try {
            return Files.list(Paths.get(SCRIPTS_DIR))
                .filter(path -> path.toString().endsWith(FILE_EXTENSION))
                .map(Path::getFileName)
                .map(Path::toString)
                .map(name -> name.substring(0, name.length() - FILE_EXTENSION.length()))
                .sorted()
                .collect(Collectors.toList());
        } catch (IOException e) {
            LOG.error("Failed to list scripts", e);
            return new ArrayList<>();
        }
    }

    /**
     * Deletes a script
     */
    public static boolean deleteScript(String scriptName) {
        try {
            String filename = sanitizeFilename(scriptName) + FILE_EXTENSION;
            Path path = Paths.get(SCRIPTS_DIR, filename);
            boolean deleted = Files.deleteIfExists(path);
            if (deleted) {
                LOG.info("Script deleted: {}", scriptName);
            }
            return deleted;
        } catch (IOException e) {
            LOG.error("Failed to delete script: {}", scriptName, e);
            return false;
        }
    }

    /**
     * Exports a script (returns .meteorplus file content)
     */
    public static String exportScript(ScriptData script) {
        return script.toMeteorPlusFormat();
    }

    /**
     * Imports a script from .meteorplus file content
     */
    public static ScriptData importScript(String content) throws IOException {
        return parseScriptContent(content);
    }

    /**
     * Checks if a script exists
     */
    public static boolean scriptExists(String scriptName) {
        String filename = sanitizeFilename(scriptName) + FILE_EXTENSION;
        return Files.exists(Paths.get(SCRIPTS_DIR, filename));
    }

    /**
     * Sanitizes filename to prevent directory traversal
     */
    private static String sanitizeFilename(String filename) {
        return filename
            .replaceAll("[^a-zA-Z0-9._-]", "_")
            .replaceAll("\\.{2,}", "_")
            .replaceAll("^_+|_+$", "");
    }

    /**
     * Gets the scripts directory path
     */
    public static Path getScriptsDirectory() {
        return Paths.get(SCRIPTS_DIR);
    }
}
