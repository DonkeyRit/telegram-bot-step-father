package com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces;

public interface Event<T, EType extends Enum<EType>> {
    T getSourceEvent();
    EType getEventType();
    Object getPayload();
}
