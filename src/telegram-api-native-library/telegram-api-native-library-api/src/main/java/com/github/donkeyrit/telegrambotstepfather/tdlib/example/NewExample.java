package com.github.donkeyrit.telegrambotstepfather.tdlib.example;

import com.github.donkeyrit.telegrambotstepfather.tdlib.events.enums.TelegramLibEventType;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.handlers.AuthorizationHandler;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventBus;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.queues.SimpleEventBus;
import com.github.donkeyrit.telegrambotstepfather.tdlib.handlers.DefaultHandler;
import com.github.donkeyrit.telegrambotstepfather.tdlib.handlers.LogMessageHandler;
import com.github.donkeyrit.telegrambotstepfather.tdlib.handlers.UpdateHandler;

import com.github.donkeyrit.telegrambotstepfather.tdlib.Client;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.io.IOException;
import java.io.IOError;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class NewExample {

    public static void main(String[] args) throws InterruptedException {

        Logger logger = LoggerFactory.getLogger(NewExample.class);

        BlockingQueue<TdApi.Function> sendRequestQueue = new LinkedBlockingQueue<>();

        EventBus<TdApi.Object, TelegramLibEventType> eventBus = new SimpleEventBus();
        eventBus.subscribe(TelegramLibEventType.UPDATE_AUTHORIZATION_STATE, new AuthorizationHandler(sendRequestQueue));

        Client.ResultHandler defaultHandler = new DefaultHandler();
        Client.ResultHandler updateHandler = new UpdateHandler(eventBus);

        // set log message handler to handle only fatal errors (0) and plain log
        // messages (-1)
        Client.setLogMessageHandler(0, new LogMessageHandler());

        // disable TDLib log and redirect fatal errors and plain log messages to a file
        try {
            Client.execute(new TdApi.SetLogVerbosityLevel(0));
            Client.execute(new TdApi.SetLogStream(new TdApi.LogStreamFile("tdlib.log", 1 << 27, false)));
        } catch (Client.ExecutionException error) {
            throw new IOError(new IOException("Write access to the current directory is required"));
        }

        // create client
        logger.info("Initializing client.");
        Client client = Client.create(updateHandler, null, null);
        logger.info("Initialization was finished.");

        while (true) {
            try {
                // Attempt to take an element from the queue (blocks if the queue is empty)
                logger.info("Wait send request");
                TdApi.Function request = sendRequestQueue.take();

                logger.info("Request with type - {} are received", request.getConstructor());

                // Send the request through the client
                client.send(request, defaultHandler);

                // Optionally, add any additional processing for the request
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
