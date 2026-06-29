package com.toolkitmc.core.api.data;

import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Typed key for tmCore data attachments.
 *
 * <pre>
 *   // Primitives with defaults
 *   TmDataKey&lt;Integer&gt; KILLS = TmDataKey.of(Identifier.of("mymod", "kills"), Integer.class, 0);
 *   TmDataKey&lt;Boolean&gt; FLAG = TmDataKey.of(Identifier.of("mymod", "flag"), Boolean.class, false);
 *   TmDataKey&lt;String&gt;  NAME = TmDataKey.of(Identifier.of("mymod", "name"), String.class, "");
 *
 *   // Lazy default (computed on first access)
 *   TmDataKey&lt;List&lt;String&gt;&gt; LIST = TmDataKey.lazy(Identifier.of("mymod", "list"), List.class, ArrayList::new);
 * </pre>
 *
 * @param <T> the value type
 */
public final class TmDataKey<T> {

    private final Identifier id;
    private final Class<T> type;
    private final Supplier<T> defaultSupplier;

    private TmDataKey(Identifier id, Class<T> type, Supplier<T> defaultSupplier) {
        this.id = Objects.requireNonNull(id, "id");
        this.type = Objects.requireNonNull(type, "type");
        this.defaultSupplier = Objects.requireNonNull(defaultSupplier, "defaultSupplier");
    }

    /** Creates a key with a constant default value. */
    public static <T> TmDataKey<T> of(Identifier id, Class<T> type, T defaultValue) {
        return new TmDataKey<>(id, type, () -> defaultValue);
    }

    /** Creates a key with a lazily-computed default (fresh instance each time). */
    public static <T> TmDataKey<T> lazy(Identifier id, Class<T> type, Supplier<T> defaultSupplier) {
        return new TmDataKey<>(id, type, defaultSupplier);
    }

    public Identifier getId() { return id; }
    public Class<T> getType() { return type; }

    /** Returns a new default value. For mutable types, always returns a fresh instance. */
    public T getDefault() { return defaultSupplier.get(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TmDataKey<?> other)) return false;
        return id.equals(other.id) && type.equals(other.type);
    }

    @Override
    public int hashCode() { return Objects.hash(id, type); }

    @Override
    public String toString() { return "TmDataKey[" + id + ", " + type.getSimpleName() + "]"; }
}
