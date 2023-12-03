package com.github.donkeyrit.telegrambotstepfather.tdlib.events.queues;

import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventHandler;
import com.github.donkeyrit.telegrambotstepfather.tdlib.example.NewExample;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventBus;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.enums.TdLibEventType;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.Event;

import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.Map;

public class SimpleEventBus implements EventBus<TdApi.Object, TdLibEventType> {

    private final Map<TdLibEventType, BlockingQueue<Event<TdApi.Object, TdLibEventType>>> eventQueues;
    private final Map<TdLibEventType, CopyOnWriteArraySet<EventHandler<TdApi.Object, TdLibEventType>>> subscribers;
    private final ExecutorService executorService;
    private final static Logger logger = LoggerFactory.getLogger(SimpleEventBus.class);

    public SimpleEventBus() {
        eventQueues = new ConcurrentHashMap<>();
        subscribers = new ConcurrentHashMap<>();
        executorService = Executors.newCachedThreadPool();
        startEventDispatchers();
    }

    private void startEventDispatchers() {
        for (TdLibEventType eventType : TdLibEventType.values()) {
            eventQueues.put(eventType, new LinkedBlockingQueue<>());
            logger.info("Submit a new task for event type - {}", eventType.toString());
            executorService.submit(() -> {
                logger.debug("Start a new task - {}", !Thread.currentThread().isInterrupted());
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Event<TdApi.Object, TdLibEventType> event = eventQueues.get(eventType).take();
                        logger.info("Notify about a new event with type - {}", event.getEventType().toString());
                        notifySubscribers(eventType, event);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore the interrupted status
                        break;
                    }
                }
            });
        }
    }

    private void notifySubscribers(TdLibEventType eventType, Event<TdApi.Object, TdLibEventType> event) {
        for (EventHandler<TdApi.Object, TdLibEventType> handler : subscribers.getOrDefault(eventType, new CopyOnWriteArraySet<>())) {
            logger.info("Notify subscriber about event");
            handler.handleEvent(event);
        }
    }

    @Override
    public void publish(Event<TdApi.Object, TdLibEventType> event) throws InterruptedException {
        logger.info("Receive event with type - {}", event.getEventType().toString());
        // TODO: make it synchronized
        eventQueues.get(event.getEventType()).put(event);
        logger.info("Put to queue");
    }

    @Override
    public void subscribe(TdLibEventType eventType, EventHandler<TdApi.Object, TdLibEventType> handler) {
        CopyOnWriteArraySet<EventHandler<TdApi.Object, TdLibEventType>> set = subscribers
            .computeIfAbsent(eventType, k -> new CopyOnWriteArraySet<>());
        set.add(handler);
    }

    @Override
    public void unsubscribe(TdLibEventType eventType, EventHandler<TdApi.Object, TdLibEventType> handler) {
        if (subscribers.containsKey(eventType)) {
            subscribers.get(eventType).remove(handler);
        }
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
        // Optionally wait for termination and handle InterruptedException
    }
}
