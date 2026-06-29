package com.toolkitmc.core.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;

import java.util.*;

/**
 * NBT helper utilities for ToolkitMC mods.
 *
 * <p>Reduces NBT boilerplate with null-safe getters and fluent builders.
 *
 * <pre>
 *   // Safe reads with defaults
 *   int level = TmNbt.getInt(nbt, "level", 0);
 *   String name = TmNbt.getString(nbt, "name", "unknown");
 *
 *   // Identifier serialization
 *   TmNbt.putIdentifier(nbt, "type", Identifier.of("mymod", "my_type"));
 *   Identifier id = TmNbt.getIdentifier(nbt, "type");
 *
 *   // List helpers
 *   List&lt;String&gt; items = TmNbt.getStringList(nbt, "items");
 *   TmNbt.putStringList(nbt, "items", List.of("a", "b", "c"));
 * </pre>
 */
public final class TmNbt {

    private TmNbt() {}

    // -------------------------------------------------------------------------
    // Safe getters — 1.21.8+: NbtCompound.get*() returns Optional<T>
    // -------------------------------------------------------------------------

    public static int getInt(NbtCompound nbt, String key, int defaultValue) {
        if (!nbt.contains(key)) return defaultValue;
        return nbt.getInt(key).orElse(defaultValue);
    }

    public static long getLong(NbtCompound nbt, String key, long defaultValue) {
        if (!nbt.contains(key)) return defaultValue;
        return nbt.getLong(key).orElse(defaultValue);
    }

    public static float getFloat(NbtCompound nbt, String key, float defaultValue) {
        if (!nbt.contains(key)) return defaultValue;
        return nbt.getFloat(key).orElse(defaultValue);
    }

    public static double getDouble(NbtCompound nbt, String key, double defaultValue) {
        if (!nbt.contains(key)) return defaultValue;
        return nbt.getDouble(key).orElse(defaultValue);
    }

    public static boolean getBoolean(NbtCompound nbt, String key, boolean defaultValue) {
        if (!nbt.contains(key)) return defaultValue;
        return nbt.getBoolean(key).orElse(defaultValue);
    }

    public static String getString(NbtCompound nbt, String key, String defaultValue) {
        if (!nbt.contains(key)) return defaultValue;
        return nbt.getString(key).orElse(defaultValue);
    }

    public static Optional<NbtCompound> getCompound(NbtCompound nbt, String key) {
        return nbt.getCompound(key);
    }

    // -------------------------------------------------------------------------
    // Identifier serialization
    // -------------------------------------------------------------------------

    public static void putIdentifier(NbtCompound nbt, String key, Identifier id) {
        nbt.putString(key, id.toString());
    }

    public static Optional<Identifier> getIdentifier(NbtCompound nbt, String key) {
        Optional<String> raw = nbt.getString(key);
        if (raw.isEmpty()) return Optional.empty();
        try {
            return Optional.of(Identifier.of(raw.get()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // -------------------------------------------------------------------------
    // String list helpers
    // -------------------------------------------------------------------------

    public static void putStringList(NbtCompound nbt, String key, List<String> values) {
        NbtList list = new NbtList();
        for (String v : values) list.add(NbtString.of(v));
        nbt.put(key, list);
    }

    public static List<String> getStringList(NbtCompound nbt, String key) {
        // getList() now returns Optional<NbtList>
        Optional<NbtList> listOpt = nbt.getList(key);
        if (listOpt.isEmpty()) return Collections.emptyList();
        NbtList list = listOpt.get();
        List<String> result = new ArrayList<>(list.size());
        for (NbtElement el : list) result.add(el.asString().orElse(""));
        return Collections.unmodifiableList(result);
    }

    // -------------------------------------------------------------------------
    // Map helpers (string → string)
    // -------------------------------------------------------------------------

    public static void putStringMap(NbtCompound nbt, String key, Map<String, String> map) {
        NbtCompound compound = new NbtCompound();
        map.forEach(compound::putString);
        nbt.put(key, compound);
    }

    public static Map<String, String> getStringMap(NbtCompound nbt, String key) {
        Optional<NbtCompound> compoundOpt = nbt.getCompound(key);
        if (compoundOpt.isEmpty()) return Collections.emptyMap();
        NbtCompound compound = compoundOpt.get();
        Map<String, String> result = new LinkedHashMap<>();
        for (String k : compound.getKeys()) {
            compound.getString(k).ifPresent(v -> result.put(k, v));
        }
        return Collections.unmodifiableMap(result);
    }

    // -------------------------------------------------------------------------
    // Merge
    // -------------------------------------------------------------------------

    /** Copies all keys from {@code source} into {@code target}, overwriting existing. */
    public static void merge(NbtCompound target, NbtCompound source) {
        for (String key : source.getKeys()) {
            NbtElement el = source.get(key);
            if (el != null) target.put(key, el.copy());
        }
    }
}
