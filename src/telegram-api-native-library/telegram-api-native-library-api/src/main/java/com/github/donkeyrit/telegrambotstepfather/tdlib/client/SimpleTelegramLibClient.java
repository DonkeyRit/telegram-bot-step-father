package com.github.donkeyrit.telegrambotstepfather.tdlib.client;

import com.github.donkeyrit.telegrambotstepfather.tdlib.client.interfaces.TelegramLibClient;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.enums.TelegramLibEventType;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventHandler;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventBus;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.queues.SimpleEventBus;
import com.github.donkeyrit.telegrambotstepfather.tdlib.handlers.DefaultHandler;
import com.github.donkeyrit.telegrambotstepfather.tdlib.handlers.LogMessageHandler;
import com.github.donkeyrit.telegrambotstepfather.tdlib.handlers.UpdateHandler;
import com.github.donkeyrit.telegrambotstepfather.tdlib.Client;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi;

import com.google.inject.Inject;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.Set;

import java.io.IOException;
import java.io.IOError;

public class SimpleTelegramLibClient implements TelegramLibClient{
    
    private static Logger logger = LoggerFactory.getLogger(SimpleTelegramLibClient.class);
    private static Client.ResultHandler defaultHandler = new DefaultHandler();

    private final BlockingQueue<TdApi.Function> sendRequestQueue;
    private final EventBus<TdApi.Object, TelegramLibEventType> eventBus;
    private final UpdateHandler updateHandler;
    private Client client;

    @Inject
    public SimpleTelegramLibClient(
        Set<EventHandler<TdApi.Object, TelegramLibEventType>> handlers, 
        BlockingQueue<TdApi.Function> sendRequestQueue) {
            
        this.sendRequestQueue = sendRequestQueue;
        this.eventBus = new SimpleEventBus();
        this.updateHandler = new UpdateHandler(eventBus);
        for (EventHandler<TdApi.Object, TelegramLibEventType> handler : handlers) {
            this.eventBus.subscribe(handler.getHandledEventType(), handler);
        }
    }

    public void initiliaze() {

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
        this.client = Client.create(updateHandler, null, null);
        logger.info("Initialization was finished.");

        logger.info("Start a separate thread for watching the sendRequestQueue.");
        new Thread(this::watchSendRequestQueue).start();
    }

    private void watchSendRequestQueue() {
        while (true) {
            try {
                // Attempt to take an element from the queue (blocks if the queue is empty)
                logger.info("Waiting for a new request in the queue");
                TdApi.Function request = sendRequestQueue.take();

                logger.info("New request with type - {} received", request.getConstructor());

                // Send the request through the client
                client.send(request, defaultHandler);

                // Optionally, add any additional processing for the request
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
