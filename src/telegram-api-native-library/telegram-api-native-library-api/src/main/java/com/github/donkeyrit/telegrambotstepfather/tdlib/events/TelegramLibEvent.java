package com.github.donkeyrit.telegrambotstepfather.tdlib.events;

import com.github.donkeyrit.telegrambotstepfather.tdlib.events.enums.TelegramLibEventType;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.Event;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi.Object;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi;

public class TelegramLibEvent implements Event<TdApi.Object, TelegramLibEventType> {

    private final TdApi.Object sourceEvent;

    public TelegramLibEvent(Object sourceEvent) {
        this.sourceEvent = sourceEvent;
    }

    public TdApi.Object getSourceEvent() {
        return sourceEvent;
    }

    @Override
    public TelegramLibEventType getEventType() {
        return TelegramLibEventType.valueOf(sourceEvent.getConstructor());
    }
}
