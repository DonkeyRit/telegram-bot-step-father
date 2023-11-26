package com.github.donkeyrit.telegrambotstepfather.tdlib.example;

import com.github.donkeyrit.telegrambotstepfather.tdlib.events.enums.TDLibEventType;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventBus;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.queues.SimpleEventBus;
import com.github.donkeyrit.telegrambotstepfather.tdlib.handlers.LogMessageHandler;
import com.github.donkeyrit.telegrambotstepfather.tdlib.handlers.UpdateHandler;

import java.io.IOError;
import java.io.IOException;

import com.github.donkeyrit.telegrambotstepfather.tdlib.Client;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi;

public class NewExample {
    
    public static void main(String[] args) {

        EventBus<TdApi.Object, TDLibEventType> eventBus = new SimpleEventBus<>();
        Client.ResultHandler updateHandler = new UpdateHandler(eventBus);

        // set log message handler to handle only fatal errors (0) and plain log messages (-1)
        Client.setLogMessageHandler(0, new LogMessageHandler());

        // disable TDLib log and redirect fatal errors and plain log messages to a file
        try {
            Client.execute(new TdApi.SetLogVerbosityLevel(0));
            Client.execute(new TdApi.SetLogStream(new TdApi.LogStreamFile("tdlib.log", 1 << 27, false)));
        } catch (Client.ExecutionException error) {
            throw new IOError(new IOException("Write access to the current directory is required"));
        }

        // create client
        Client client = Client.create(updateHandler, null, null);
    }
}
