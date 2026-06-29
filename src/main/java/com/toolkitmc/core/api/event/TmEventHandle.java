package com.toolkitmc.core.api.event;

/**
 * Handle returned by {@link TmEventBus#subscribe} that allows unsubscribing.
 *
 * <pre>
 *   TmEventHandle h = TmCore.events().subscribe(MyEvent.class, e -> { ... });
 *   // later:
 *   h.unsubscribe();
 * </pre>
 */
@FunctionalInterface
public interface TmEventHandle {
    /** Removes this subscription from the event bus. Safe to call multiple times. */
    void unsubscribe();
}
