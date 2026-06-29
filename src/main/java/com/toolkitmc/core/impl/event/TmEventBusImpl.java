package com.toolkitmc.core.impl.event;

import com.toolkitmc.core.TmCore;
import com.toolkitmc.core.api.event.Cancellable;
import com.toolkitmc.core.api.event.TmEventBus;
import com.toolkitmc.core.api.event.TmEventHandle;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class TmEventBusImpl implements TmEventBus {

    private final Map<Class<?>, List<Subscription<?>>> subscribers = new ConcurrentHashMap<>();

    @Override
    public <T> TmEventHandle subscribe(Class<T> eventType, Consumer<T> listener, int priority) {
        Objects.requireNonNull(eventType, "eventType");
        Objects.requireNonNull(listener, "listener");

        List<Subscription<?>> list = subscribers.computeIfAbsent(
            eventType, k -> new CopyOnWriteArrayList<>()
        );

        Subscription<T> sub = new Subscription<>(listener, priority);
        list.add(sub);
        // Sort descending by priority after each insertion.
        // Explicit Comparator<Subscription<?>> cast avoids wildcard capture issue
        // with method reference Subscription::priority in Java generics.
        list.sort(Comparator.<Subscription<?>, Integer>comparing(s -> s.priority()).reversed());

        return () -> list.remove(sub);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T fire(T event) {
        Objects.requireNonNull(event, "event");
        List<Subscription<?>> list = subscribers.get(event.getClass());
        if (list == null || list.isEmpty()) return event;

        for (Subscription<?> sub : list) {
            try {
                ((Subscription<T>) sub).listener().accept(event);
            } catch (Exception e) {
                TmCore.LOGGER.error(
                    "Exception in TmEventBus subscriber for {}: {}",
                    event.getClass().getSimpleName(), e.getMessage(), e
                );
            }
        }
        return event;
    }

    @Override
    public <T extends Cancellable> boolean fireAndCheck(T event) {
        fire(event);
        return !event.isCancelled();
    }

    @Override
    public void clearSubscribers(Class<?> eventType) {
        List<Subscription<?>> list = subscribers.get(eventType);
        if (list != null) list.clear();
    }

    @Override
    public int subscriberCount(Class<?> eventType) {
        List<Subscription<?>> list = subscribers.get(eventType);
        return list == null ? 0 : list.size();
    }

    // -------------------------------------------------------------------------

    private record Subscription<T>(Consumer<T> listener, int priority) {}
}
