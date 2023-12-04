package com.github.donkeyrit.telegrambotstepfather.tdlib.events.queues;

import com.github.donkeyrit.telegrambotstepfather.tdlib.events.enums.TelegramLibEventType;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventHandler;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventBus;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.Event;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.Map;

public class SimpleEventBus implements EventBus<TdApi.Object, TelegramLibEventType> {

    private final Map<TelegramLibEventType, BlockingQueue<Event<TdApi.Object, TelegramLibEventType>>> eventQueues;
    private final Map<TelegramLibEventType, CopyOnWriteArraySet<EventHandler<TdApi.Object, TelegramLibEventType>>> subscribers;
    private final ExecutorService executorService;
    private final static Logger logger = LoggerFactory.getLogger(SimpleEventBus.class);

    public SimpleEventBus() {
        eventQueues = new ConcurrentHashMap<>();
        subscribers = new ConcurrentHashMap<>();
        executorService = Executors.newCachedThreadPool();
        startEventDispatchers();
    }

    private void startEventDispatchers() {
        for (TelegramLibEventType eventType : TelegramLibEventType.values()) {
            eventQueues.put(eventType, new LinkedBlockingQueue<>());
            logger.info("Submit a new task for event type - {}", eventType.toString());
            executorService.submit(() -> {
                logger.debug("Start a new task - {}", !Thread.currentThread().isInterrupted());
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Event<TdApi.Object, TelegramLibEventType> event = eventQueues.get(eventType).take();
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

    private void notifySubscribers(TelegramLibEventType eventType, Event<TdApi.Object, TelegramLibEventType> event) {
        for (EventHandler<TdApi.Object, TelegramLibEventType> handler : subscribers.getOrDefault(eventType, new CopyOnWriteArraySet<>())) {
            logger.info("Notify subscriber about event");
            handler.handleEvent(event);
        }
    }

    @Override
    public void publish(Event<TdApi.Object, TelegramLibEventType> event) throws InterruptedException {
        logger.info("Receive event with type - {}", event.getEventType().toString());
        eventQueues.get(event.getEventType()).put(event);
        logger.info("Put to queue");
    }

    @Override
    public void subscribe(TelegramLibEventType eventType, EventHandler<TdApi.Object, TelegramLibEventType> handler) {
        CopyOnWriteArraySet<EventHandler<TdApi.Object, TelegramLibEventType>> set = subscribers
            .computeIfAbsent(eventType, k -> new CopyOnWriteArraySet<>());
        set.add(handler);
    }

    @Override
    public void unsubscribe(TelegramLibEventType eventType, EventHandler<TdApi.Object, TelegramLibEventType> handler) {
        if (subscribers.containsKey(eventType)) {
            subscribers.get(eventType).remove(handler);
        }
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
        try {
            // Optionally wait for termination and handle InterruptedException
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt(); // Restore the interrupted status
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            logger.error("Thread interrupted while waiting for termination.", e);
        }
    }
}
