package com.github.donkeyrit.telegrambotstepfather.tdlib.events.enums;

import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi;
import java.util.HashMap;
import java.util.Map;

public enum TdLibEventType {

    UPDATE_AUTHORIZATION_STATE(TdApi.UpdateAuthorizationState.CONSTRUCTOR);

    private static final Map<Integer, TdLibEventType> ID_MAP = new HashMap<>();

    static {
        for (TdLibEventType eventType : values()) {
            ID_MAP.put(eventType.constructorId, eventType);
        }
    }

    private final int constructorId;

    TdLibEventType(int constructorId) {
        this.constructorId = constructorId;
    }

    public static TdLibEventType valueOf(int constructorId) {
        return ID_MAP.get(constructorId);
    }
}