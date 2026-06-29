package com.toolkitmc.core.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Fluent Text builder utilities for ToolkitMC mods.
 *
 * <pre>
 *   Text msg = TmText.of("Hello ").bold().red()
 *       .append(TmText.of(player.getName().getString()).gold())
 *       .build();
 *
 *   Text prefix = TmText.prefix("MyMod");  // → "[MyMod] " in gold
 * </pre>
 */
public final class TmText {

    private final MutableText text;

    private TmText(MutableText text) { this.text = text; }

    public static TmText of(String content) {
        return new TmText(Text.literal(content));
    }

    public static TmText translatable(String key, Object... args) {
        return new TmText(Text.translatable(key, args));
    }

    /** Creates "[ModName] " prefix in gold brackets with white mod name. */
    public static Text prefix(String modName) {
        return Text.literal("[")
            .formatted(Formatting.GRAY)
            .append(Text.literal(modName).formatted(Formatting.GOLD))
            .append(Text.literal("] ").formatted(Formatting.GRAY));
    }

    /** Creates a colored success message: "✔ <message>" in green. */
    public static Text success(String message) {
        return Text.literal("✔ " + message).formatted(Formatting.GREEN);
    }

    /** Creates a colored error message: "✘ <message>" in red. */
    public static Text error(String message) {
        return Text.literal("✘ " + message).formatted(Formatting.RED);
    }

    /** Creates a warning message: "⚠ <message>" in yellow. */
    public static Text warning(String message) {
        return Text.literal("⚠ " + message).formatted(Formatting.YELLOW);
    }

    // -------------------------------------------------------------------------
    // Fluent formatting
    // -------------------------------------------------------------------------

    public TmText bold()          { text.formatted(Formatting.BOLD); return this; }
    public TmText italic()        { text.formatted(Formatting.ITALIC); return this; }
    public TmText underline()     { text.formatted(Formatting.UNDERLINE); return this; }
    public TmText strikethrough() { text.formatted(Formatting.STRIKETHROUGH); return this; }
    public TmText obfuscated()    { text.formatted(Formatting.OBFUSCATED); return this; }
    public TmText reset()         { text.formatted(Formatting.RESET); return this; }

    public TmText black()       { text.formatted(Formatting.BLACK); return this; }
    public TmText darkBlue()    { text.formatted(Formatting.DARK_BLUE); return this; }
    public TmText darkGreen()   { text.formatted(Formatting.DARK_GREEN); return this; }
    public TmText darkAqua()    { text.formatted(Formatting.DARK_AQUA); return this; }
    public TmText darkRed()     { text.formatted(Formatting.DARK_RED); return this; }
    public TmText darkPurple()  { text.formatted(Formatting.DARK_PURPLE); return this; }
    public TmText gold()        { text.formatted(Formatting.GOLD); return this; }
    public TmText gray()        { text.formatted(Formatting.GRAY); return this; }
    public TmText darkGray()    { text.formatted(Formatting.DARK_GRAY); return this; }
    public TmText blue()        { text.formatted(Formatting.BLUE); return this; }
    public TmText green()       { text.formatted(Formatting.GREEN); return this; }
    public TmText aqua()        { text.formatted(Formatting.AQUA); return this; }
    public TmText red()         { text.formatted(Formatting.RED); return this; }
    public TmText lightPurple() { text.formatted(Formatting.LIGHT_PURPLE); return this; }
    public TmText yellow()      { text.formatted(Formatting.YELLOW); return this; }
    public TmText white()       { text.formatted(Formatting.WHITE); return this; }

    public TmText style(Style style)  { text.setStyle(style); return this; }
    public TmText append(Text other)  { text.append(other); return this; }
    public TmText append(TmText other){ text.append(other.text); return this; }

    public MutableText build() { return text; }
    public Text get()          { return text; }

    @Override
    public String toString() { return text.getString(); }
}
