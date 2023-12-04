package com.github.donkeyrit.telegrambotstepfather.tdlib.example;

import com.github.donkeyrit.telegrambotstepfather.tdlib.ioc.TelegramApiNativeLibraryApiModules;
import com.github.donkeyrit.telegrambotstepfather.tdlib.client.interfaces.TelegramLibClient;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class NewExample {

    public static void main(String[] args) throws InterruptedException {

        Injector injector = Guice.createInjector(new TelegramApiNativeLibraryApiModules());
        TelegramLibClient client = injector.getInstance(TelegramLibClient.class);
        client.initiliaze();
    }
}
