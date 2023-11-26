package com.github.donkeyrit.telegrambotstepfather.tdlib.handlers;

import com.github.donkeyrit.telegrambotstepfather.tdlib.Client;

public class LogMessageHandler implements Client.LogMessageHandler {
    @Override
    public void onLogMessage(int verbosityLevel, String message) {
        if (verbosityLevel == 0) {
            return;
        }
        System.err.println(message);
    }
}
