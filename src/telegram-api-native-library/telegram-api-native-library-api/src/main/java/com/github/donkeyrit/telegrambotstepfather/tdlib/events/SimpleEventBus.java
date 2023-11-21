package com.github.donkeyrit.telegrambotstepfather.tdlib.events;

import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventBus;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.Event;

public class SimpleEventBus<T, EType extends Enum<EType>> implements EventBus<T, EType> {

    @Override
    public void publish(Event<T, EType> event) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'publish'");
    }

    @Override
    public void subscribe(Object subscriber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'subscribe'");
    }
    
}
