package com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces;

public interface EventBus<T, EType extends Enum<EType>> {
    void publish(Event<T, EType> event) throws InterruptedException;
}
