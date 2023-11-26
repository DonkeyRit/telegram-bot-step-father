package com.github.donkeyrit.telegrambotstepfather.tdlib.example;

import com.github.donkeyrit.telegrambotstepfather.tdlib.events.enums.TDLibEventType;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventBus;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.queues.SimpleEventBus;
import com.github.donkeyrit.telegrambotstepfather.tdlib.handlers.LogMessageHandler;
import com.github.donkeyrit.telegrambotstepfather.tdlib.handlers.UpdateHandler;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.donkeyrit.telegrambotstepfather.tdlib.Client;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi;

public class NewExample {

    public static void main(String[] args) throws InterruptedException {

        EventBus<TdApi.Object, TDLibEventType> eventBus = new SimpleEventBus<>();
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
        Client client = Client.create(updateHandler, null, null);
        Thread.sleep(10000);
        SendTdLibAuthParameters(client);
        Thread.sleep(10000);
        SendPhoneNumber(client);
        Thread.sleep(10000);
        SendVerificationCode(client);
        Thread.sleep(10000);
    }

    private static void SendTdLibAuthParameters(Client client) {
        TdApi.SetTdlibParameters request = new TdApi.SetTdlibParameters();
        request.databaseDirectory = "tdlib";
        request.useMessageDatabase = true;
        request.useSecretChats = true;
        request.apiId = 94575;
        request.apiHash = "a3406de8d171bb422bb6ddf3bbd800e2";
        request.systemLanguageCode = "en";
        request.deviceModel = "Desktop";
        request.applicationVersion = "1.0";
        request.enableStorageOptimizer = true;

        client.send(request, null);
    }

    public static void SendPhoneNumber(Client client) {
        String phoneNumber = promptString("Please enter phone number: ");
        client.send(new TdApi.SetAuthenticationPhoneNumber(phoneNumber, null), null);
    }

    public static void SendVerificationCode(Client client) {
        String code = promptString("Please enter authentication code: ");
        client.send(new TdApi.CheckAuthenticationCode(code), null);
    }

    private static String promptString(String prompt) {
        System.out.print(prompt);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String str = "";
        try {
            str = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
