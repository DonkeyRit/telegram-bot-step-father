package com.github.donkeyrit.telegrambotstepfather.tdlib.handlers;

import com.github.donkeyrit.telegrambotstepfather.tdlib.events.enums.TelegramLibEventType;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventBus;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.TelegramLibEvent;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi.Object;
import com.github.donkeyrit.telegrambotstepfather.tdlib.Client;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateHandler implements Client.ResultHandler {

    private static final List<Integer> SUPPORTED_TDLIB_EVENT_TYPES = Arrays
            .asList(TdApi.UpdateAuthorizationState.CONSTRUCTOR);

    private final EventBus<TdApi.Object, TelegramLibEventType> eventBus;
    private static final Logger logger = LoggerFactory.getLogger(UpdateHandler.class);

    public UpdateHandler(EventBus<Object, TelegramLibEventType> eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void onResult(TdApi.Object object) {
        if (SUPPORTED_TDLIB_EVENT_TYPES.contains(object.getConstructor())) {
            try {
                logger.info("Receive event with type - {}", object.getConstructor());
                eventBus.publish(new TelegramLibEvent(object));
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }   
    }
}
