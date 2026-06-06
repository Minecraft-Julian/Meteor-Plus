package com.meteorplus.scripting.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.MinecraftClient;

/**
 * Manages reading and writing script files to disk.
 */
public class ScriptManager {
    public static final String SCRIPT_EXTENSION = ".meteorplus";
    private final Path scriptsDirectory;

    public ScriptManager(Path scriptsDirectory) {
        this.scriptsDirectory = scriptsDirectory;
        createScriptsFolder();
    }

    public ScriptManager(MinecraftClient client) {
        this(client == null ? Path.of("meteorplus", "scripts") : client.runDirectory("meteorplus/scripts").toPath());
    }

    private void createScriptsFolder() {
        try {
            if (!Files.exists(scriptsDirectory)) {
                Files.createDirectories(scriptsDirectory);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create script storage folder: " + scriptsDirectory, e);
        }
    }

    public List<ScriptData> loadAllScripts() {
        List<ScriptData> scripts = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(scriptsDirectory, "*" + SCRIPT_EXTENSION)) {
            for (Path path : stream) {
                ScriptData script = loadScript(path);
                if (script != null) {
                    scripts.add(script);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read script files", e);
        }
        return scripts;
    }

    public ScriptData loadScript(String scriptName) {
        if (scriptName == null) {
            return null;
        }
        Path path = scriptsDirectory.resolve(scriptName + SCRIPT_EXTENSION);
        if (!Files.exists(path)) {
            return null;
        }
        return loadScript(path);
    }

    private ScriptData loadScript(Path path) {
        try {
            String content = Files.readString(path, StandardCharsets.UTF_8);
            return ScriptData.fromJson(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load script: " + path, e);
        }
    }

    public void saveScript(ScriptData scriptData) {
        if (scriptData == null || scriptData.getName() == null) {
            throw new IllegalArgumentException("Script data or name is missing");
        }
        scriptData.setLastModified(System.currentTimeMillis());
        Path path = scriptsDirectory.resolve(scriptData.getName() + SCRIPT_EXTENSION);
        String content = scriptData.toJson();
        try {
            Files.writeString(path, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save script: " + path, e);
        }
    }

    public boolean deleteScript(String scriptName) {
        if (scriptName == null) {
            return false;
        }
        Path path = scriptsDirectory.resolve(scriptName + SCRIPT_EXTENSION);
        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete script: " + path, e);
        }
    }

    public void exportScript(String scriptName, Path targetPath) {
        ScriptData scriptData = loadScript(scriptName);
        if (scriptData == null || targetPath == null) {
            return;
        }
        try {
            Files.writeString(targetPath, scriptData.toJson(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to export script: " + targetPath, e);
        }
    }

    public void importScript(Path sourcePath) {
        if (sourcePath == null || !Files.exists(sourcePath)) {
            throw new IllegalArgumentException("Source path does not exist");
        }
        try {
            String content = Files.readString(sourcePath, StandardCharsets.UTF_8);
            ScriptData scriptData = ScriptData.fromJson(content);
            if (scriptData == null || scriptData.getName() == null) {
                throw new IllegalArgumentException("Invalid script file");
            }
            Path destination = scriptsDirectory.resolve(scriptData.getName() + SCRIPT_EXTENSION);
            Files.writeString(destination, scriptData.toJson(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to import script: " + sourcePath, e);
        }
    }

    public List<String> getScriptNames() {
        List<String> names = new ArrayList<>();
        for (ScriptData script : loadAllScripts()) {
            names.add(script.getName());
        }
        return names;
    }
}
