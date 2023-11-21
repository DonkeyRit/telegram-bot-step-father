package com.github.donkeyrit.telegrambotstepfather.tdlib.events;

import com.github.donkeyrit.telegrambotstepfather.tdlib.events.enums.TDLibEventType;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.Event;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi.Object;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi;

public class TDLibEvent implements Event<TdApi.Object, TDLibEventType> {

    private final TdApi.Object sourceEvent;

    public TDLibEvent(Object sourceEvent) {
        this.sourceEvent = sourceEvent;
    }

    public TdApi.Object getSourceEvent() {
        return sourceEvent;
    }

    @Override
    public TDLibEventType getEventType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEventType'");
    }

    @Override
    public java.lang.Object getPayload() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPayload'");
    }
}
