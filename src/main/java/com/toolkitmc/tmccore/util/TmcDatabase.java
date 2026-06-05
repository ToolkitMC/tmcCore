package com.toolkitmc.tmccore.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;

public class TmcDatabase {
    public static JsonObject loadJson(Path path) {
        try (FileReader reader = new FileReader(path.toFile())) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (Exception e) {
            return new JsonObject();
        }
    }

    public static void saveJson(Path path, JsonObject json) {
        try (FileWriter writer = new FileWriter(path.toFile())) {
            TmcUtils.GSON.toJson(json, writer);
        } catch (Exception ignored) {}
    }
}