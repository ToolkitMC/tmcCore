package com.toolkitmc.core.api.event;

import java.util.function.Consumer;

/**
 * Custom event bus for ToolkitMC mods.
 *
 * <p>Unlike Fabric's static event fields, this bus uses dynamic event types
 * identified by class. Any object can be an event.
 *
 * <pre>
 *   // Define a custom event record
 *   public record PlayerJoinTmEvent(ServerPlayerEntity player, MinecraftServer server) {}
 *
 *   // Subscribe (returns a handle for unsubscribing)
 *   TmEventHandle handle = TmCore.events().subscribe(PlayerJoinTmEvent.class, event -> {
 *       TmCore.LOGGER.info("{} joined!", event.player().getName().getString());
 *   });
 *
 *   // Fire
 *   TmCore.events().fire(new PlayerJoinTmEvent(player, server));
 *
 *   // Unsubscribe
 *   handle.unsubscribe();
 * </pre>
 *
 * <p>Subscribers are called in registration order. Exceptions in one subscriber
 * do not stop others — errors are logged and execution continues.
 *
 * <p>Thread safety: subscribers are always called on the thread that fires the event.
 * Do not fire server-side events from client threads.
 */
public interface TmEventBus {

    /**
     * Subscribes a listener for the given event type.
     *
     * @param eventType the class of the event to listen for
     * @param listener  the callback invoked when this event fires
     * @param priority  subscription priority (higher fires first)
     * @return a handle that can unsubscribe this listener
     */
    <T> TmEventHandle subscribe(Class<T> eventType, Consumer<T> listener, int priority);

    /**
     * Subscribes with default priority (0).
     */
    default <T> TmEventHandle subscribe(Class<T> eventType, Consumer<T> listener) {
        return subscribe(eventType, listener, 0);
    }

    /**
     * Fires an event to all registered subscribers.
     *
     * @param event the event instance
     * @return the same event instance (allows chaining / reading mutations)
     */
    <T> T fire(T event);

    /**
     * Fires a cancellable event. Returns {@code true} if the event was NOT cancelled.
     *
     * @param event must implement {@link Cancellable}
     */
    <T extends Cancellable> boolean fireAndCheck(T event);

    /**
     * Removes all subscribers for the given event type.
     * Useful for cleanup during mod unload or test teardown.
     */
    void clearSubscribers(Class<?> eventType);

    /**
     * Returns the count of active subscribers for the given event type.
     */
    int subscriberCount(Class<?> eventType);
}
