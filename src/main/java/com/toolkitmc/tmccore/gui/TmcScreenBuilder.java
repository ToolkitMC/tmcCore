package com.toolkitmc.tmccore.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class TmcScreenBuilder {

    public static Screen createSimpleScreen(String title, ButtonWidget... buttons) {
        return new Screen(Text.literal(title)) {
            @Override
            protected void init() {
                int y = 50;
                for (ButtonWidget button : buttons) {
                    this.addDrawableChild(button);
                    button.setY(y);
                    y += 30;
                }
            }
        };
    }
}