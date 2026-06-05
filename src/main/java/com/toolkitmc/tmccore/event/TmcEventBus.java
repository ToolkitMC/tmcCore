package com.toolkitmc.tmccore.event;

import java.util.*;
import java.util.function.Consumer;

public class TmcEventBus {

    private final Map<Class<?>, List<Consumer<?>>> listeners = new HashMap<>();
    private final Map<Class<?>, List<Consumer<?>>> priorityListeners = new HashMap<>();

    public <T> void register(Class<T> eventType, Consumer<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public <T> void registerPriority(Class<T> eventType, Consumer<T> listener) {
        priorityListeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    @SuppressWarnings("unchecked")
    public <T> void post(T event) {
        // Önce priority listener'lar çalışsın
        List<Consumer<?>> priorityList = priorityListeners.get(event.getClass());
        if (priorityList != null) {
            for (Consumer<?> listener : priorityList) {
                ((Consumer<T>) listener).accept(event);
            }
        }

        // Sonra normal listener'lar
        List<Consumer<?>> list = listeners.get(event.getClass());
        if (list != null) {
            for (Consumer<?> listener : list) {
                ((Consumer<T>) listener).accept(event);
            }
        }
    }

    public void clear() {
        listeners.clear();
        priorityListeners.clear();
    }
}