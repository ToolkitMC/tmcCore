package com.toolkitmc.tmccore.sandbox;

import com.toolkitmc.tmccore.TmcCore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TmcFileManager {

    public static String readFile(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            TmcCore.LOGGER.error("Failed to read file: {}", path, e);
            return "";
        }
    }

    public static void writeFile(Path path, String content) {
        try {
            Files.writeString(path, content);
        } catch (IOException e) {
            TmcCore.LOGGER.error("Failed to write file: {}", path, e);
        }
    }

    public static boolean fileExists(Path path) {
        return Files.exists(path);
    }

    public static void createDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            TmcCore.LOGGER.error("Failed to create directory: {}", path, e);
        }
    }
}