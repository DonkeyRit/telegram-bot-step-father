package com.github.donkeyrit.telegrambotstepfather.tdlib.events.enums;

import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi;
import java.util.HashMap;
import java.util.Map;

public enum TelegramLibEventType {

    UPDATE_AUTHORIZATION_STATE(TdApi.UpdateAuthorizationState.CONSTRUCTOR);

    private static final Map<Integer, TelegramLibEventType> ID_MAP = new HashMap<>();

    static {
        for (TelegramLibEventType eventType : values()) {
            ID_MAP.put(eventType.constructorId, eventType);
        }
    }

    private final int constructorId;

    TelegramLibEventType(int constructorId) {
        this.constructorId = constructorId;
    }

    public static TelegramLibEventType valueOf(int constructorId) {
        return ID_MAP.get(constructorId);
    }
}