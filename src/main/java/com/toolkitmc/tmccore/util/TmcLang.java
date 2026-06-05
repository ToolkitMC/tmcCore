package com.toolkitmc.tmccore.util;

import net.minecraft.text.Text;

public class TmcLang {
    public static Text translate(String key, Object... args) {
        return Text.translatable(key, args);
    }

    public static String get(String key) {
        return Text.translatable(key).getString();
    }
}