package com.github.donkeyrit.telegrambotstepfather.tdlib.events.handlers;

import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.EventHandler;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.enums.TdLibEventType;
import com.github.donkeyrit.telegrambotstepfather.tdlib.events.interfaces.Event;
import com.github.donkeyrit.telegrambotstepfather.tdlib.Client;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class AuthorizationHandler implements EventHandler<TdApi.Object, TdLibEventType> {

    @Override
    public void handleEvent(Event<TdApi.Object, TdLibEventType> event) {
        TdApi.AuthorizationState authorizationState = ((TdApi.UpdateAuthorizationState) event.getSourceEvent()).authorizationState;
        switch (authorizationState.getConstructor()) {
            case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR:
                sendTdLibAuthParameters(null);
                break;
            case TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR:
                sendPhoneNumber(null);
                break;
            case TdApi.AuthorizationStateWaitCode.CONSTRUCTOR:
                sendVerificationCode(null);
                break;
            case TdApi.AuthorizationStateReady.CONSTRUCTOR:
                break;
            default:
                break;
        }
    }
    
    private static TdApi.Function sendTdLibAuthParameters(Client client) {
        TdApi.SetTdlibParameters request = new TdApi.SetTdlibParameters();
        request.databaseDirectory = "tdlib";
        request.useMessageDatabase = true;
        request.useSecretChats = true;
        // Secrets
        request.apiId = 26585478;
        request.apiHash = "9e89858a14ae7f27eedef7218937fb7f";
        // Secrets
        request.systemLanguageCode = "en";
        request.deviceModel = "Desktop";
        request.applicationVersion = "1.0";
        request.enableStorageOptimizer = true;

        return request;
    }

    public static TdApi.Function sendPhoneNumber(Client client) {
        String phoneNumber = promptString("Please enter phone number: ");
        return new TdApi.SetAuthenticationPhoneNumber(phoneNumber, null);
    }

    public static TdApi.Function sendVerificationCode(Client client) {
        String code = promptString("Please enter authentication code: ");
        return new TdApi.CheckAuthenticationCode(code);
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
