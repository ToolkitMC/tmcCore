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
    // Safe getters
    // -------------------------------------------------------------------------

    public static int getInt(NbtCompound nbt, String key, int defaultValue) {
        return nbt.contains(key, NbtElement.INT_TYPE) ? nbt.getInt(key) : defaultValue;
    }

    public static long getLong(NbtCompound nbt, String key, long defaultValue) {
        return nbt.contains(key, NbtElement.LONG_TYPE) ? nbt.getLong(key) : defaultValue;
    }

    public static float getFloat(NbtCompound nbt, String key, float defaultValue) {
        return nbt.contains(key, NbtElement.FLOAT_TYPE) ? nbt.getFloat(key) : defaultValue;
    }

    public static double getDouble(NbtCompound nbt, String key, double defaultValue) {
        return nbt.contains(key, NbtElement.DOUBLE_TYPE) ? nbt.getDouble(key) : defaultValue;
    }

    public static boolean getBoolean(NbtCompound nbt, String key, boolean defaultValue) {
        return nbt.contains(key, NbtElement.BYTE_TYPE) ? nbt.getBoolean(key) : defaultValue;
    }

    public static String getString(NbtCompound nbt, String key, String defaultValue) {
        return nbt.contains(key, NbtElement.STRING_TYPE) ? nbt.getString(key) : defaultValue;
    }

    public static Optional<NbtCompound> getCompound(NbtCompound nbt, String key) {
        return nbt.contains(key, NbtElement.COMPOUND_TYPE)
            ? Optional.of(nbt.getCompound(key))
            : Optional.empty();
    }

    // -------------------------------------------------------------------------
    // Identifier serialization
    // -------------------------------------------------------------------------

    public static void putIdentifier(NbtCompound nbt, String key, Identifier id) {
        nbt.putString(key, id.toString());
    }

    public static Optional<Identifier> getIdentifier(NbtCompound nbt, String key) {
        if (!nbt.contains(key, NbtElement.STRING_TYPE)) return Optional.empty();
        try {
            return Optional.of(Identifier.of(nbt.getString(key)));
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
        if (!nbt.contains(key, NbtElement.LIST_TYPE)) return Collections.emptyList();
        NbtList list = nbt.getList(key, NbtElement.STRING_TYPE);
        List<String> result = new ArrayList<>(list.size());
        for (NbtElement el : list) result.add(el.asString());
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
        if (!nbt.contains(key, NbtElement.COMPOUND_TYPE)) return Collections.emptyMap();
        NbtCompound compound = nbt.getCompound(key);
        Map<String, String> result = new LinkedHashMap<>();
        for (String k : compound.getKeys()) {
            if (compound.contains(k, NbtElement.STRING_TYPE)) {
                result.put(k, compound.getString(k));
            }
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
