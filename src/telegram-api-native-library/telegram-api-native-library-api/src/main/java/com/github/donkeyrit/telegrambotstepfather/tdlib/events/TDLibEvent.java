package com.github.donkeyrit.telegrambotstepfather.tdlib.events;

import com.github.donkeyrit.telegrambotstepfather.tdlib.events.enums.TdLibEventType;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.Event;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi.Object;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi;

public class TdLibEvent implements Event<TdApi.Object, TdLibEventType> {

    private final TdApi.Object sourceEvent;

    public TdLibEvent(Object sourceEvent) {
        this.sourceEvent = sourceEvent;
    }

    public TdApi.Object getSourceEvent() {
        return sourceEvent;
    }

    @Override
    public TdLibEventType getEventType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEventType'");
    }

    @Override
    public java.lang.Object getPayload() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPayload'");
    }
}
