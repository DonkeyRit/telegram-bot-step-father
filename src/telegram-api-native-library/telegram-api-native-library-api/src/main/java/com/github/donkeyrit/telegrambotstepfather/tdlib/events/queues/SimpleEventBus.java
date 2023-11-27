package com.github.donkeyrit.telegrambotstepfather.tdlib.events.queues;

import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventHandler;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventBus;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.Event;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.Map;

public class SimpleEventBus<T, EType extends Enum<EType>> implements EventBus<T, EType> {

    private final Map<EType, BlockingQueue<Event<T, EType>>> eventQueues;
    private final Map<EType, CopyOnWriteArraySet<EventHandler<T, EType>>> subscribers;

    public SimpleEventBus() {
        eventQueues = new ConcurrentHashMap<>();
        subscribers = new ConcurrentHashMap<>();
    }

    @Override
    public void publish(Event<T, EType> event) throws InterruptedException {
        BlockingQueue<Event<T, EType>> queue = eventQueues
            .computeIfAbsent(event.getEventType(), k -> new LinkedBlockingQueue<>());
        queue.put(event);
    }

    @Override
    public void subscribe(EType eventType, EventHandler<T, EType> handler) {
        CopyOnWriteArraySet<EventHandler<T, EType>> set = subscribers
            .computeIfAbsent(eventType, k -> new CopyOnWriteArraySet<>());
        set.add(handler);
    }

    @Override
    public void unsubscribe(EType eventType, EventHandler<T, EType> handler) {
        if (subscribers.containsKey(eventType)) {
            subscribers.get(eventType).remove(handler);
        }
    }
}
