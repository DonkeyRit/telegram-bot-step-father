package com.github.donkeyrit.telegrambotstepfather.tdlib.handlers;

import com.github.donkeyrit.telegrambotstepfather.tdlib.events.enums.TdLibEventType;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventBus;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.TdLibEvent;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi.Object;
import com.github.donkeyrit.telegrambotstepfather.tdlib.Client;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi;

import java.util.Arrays;
import java.util.List;

public class UpdateHandler implements Client.ResultHandler {

    private static final List<Integer> SUPPORTED_TDLIB_EVENT_TYPES = Arrays
            .asList(TdApi.UpdateAuthorizationState.CONSTRUCTOR);

    private final EventBus<TdApi.Object, TdLibEventType> eventBus;

    public UpdateHandler(EventBus<Object, TdLibEventType> eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void onResult(TdApi.Object object) {
        if (SUPPORTED_TDLIB_EVENT_TYPES.contains(object.getConstructor())) {
            try {
                eventBus.publish(new TdLibEvent(object));
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }   
    }
}
