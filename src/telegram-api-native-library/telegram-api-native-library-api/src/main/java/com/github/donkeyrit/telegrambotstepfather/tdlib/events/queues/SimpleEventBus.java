package com.github.donkeyrit.telegrambotstepfather.tdlib.events.queues;

import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventBus;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.Event;

public class SimpleEventBus<T, EType extends Enum<EType>> implements EventBus<T, EType> {

    private BlockingQueue<Event<T, EType>> queue;

    public SimpleEventBus() {
        queue = new ArrayBlockingQueue<Event<T, EType>>(100);
    }

    @Override
    public void publish(Event<T, EType> event) throws InterruptedException {
        queue.put(event);
    }
}
