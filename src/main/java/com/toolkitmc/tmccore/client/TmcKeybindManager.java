package com.toolkitmc.tmccore.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class TmcKeybindManager {
    private static final List<KeyBinding> keybinds = new ArrayList<>();

    public static KeyBinding register(String name, int key) {
        KeyBinding kb = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.tmccore." + name,
            InputUtil.Type.KEYSYM,
            key,
            "category.tmccore"
        ));
        keybinds.add(kb);
        return kb;
    }

    public static void init() {
        // Default keybinds
        register("open_menu", GLFW.GLFW_KEY_K);
    }
}