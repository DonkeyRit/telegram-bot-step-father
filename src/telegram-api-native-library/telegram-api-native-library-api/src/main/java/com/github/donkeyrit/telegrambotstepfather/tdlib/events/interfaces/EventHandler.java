package com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces;

public interface EventHandler<T, EType extends Enum<EType>> {
    void handleEvent(Event<T, EType> event);
}