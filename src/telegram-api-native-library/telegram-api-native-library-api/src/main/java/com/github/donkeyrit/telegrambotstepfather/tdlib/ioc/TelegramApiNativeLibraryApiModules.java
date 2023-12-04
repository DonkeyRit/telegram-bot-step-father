package com.github.donkeyrit.telegrambotstepfather.tdlib.ioc;

import com.github.donkeyrit.telegrambotstepfather.tdlib.events.handlers.AuthorizationHandler;
import com.github.donkeyrit.telegrambotstepfather.tdlib.client.interfaces.TelegramLibClient;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventHandler;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.enums.TelegramLibEventType;
import com.github.donkeyrit.telegrambotstepfather.tdlib.client.SimpleTelegramLibClient;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi;

import com.google.inject.multibindings.Multibinder;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.Provides;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TelegramApiNativeLibraryApiModules extends AbstractModule {
    
    @Override
    protected void configure() {
        Multibinder
            .newSetBinder(binder(), new TypeLiteral<EventHandler<TdApi.Object, TelegramLibEventType>>() {})
            .addBinding()
            .to(AuthorizationHandler.class);

        bind(TelegramLibClient.class).to(SimpleTelegramLibClient.class);
    }

    @Provides
    public BlockingQueue<TdApi.Function> provideSendRequestQueue() {
        return new LinkedBlockingQueue<>();
    }
}
