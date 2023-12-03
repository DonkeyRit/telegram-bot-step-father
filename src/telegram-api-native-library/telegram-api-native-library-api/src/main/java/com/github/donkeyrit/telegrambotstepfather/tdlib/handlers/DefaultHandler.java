package com.github.donkeyrit.telegrambotstepfather.tdlib.handlers;

import com.github.donkeyrit.telegrambotstepfather.tdlib.Client;
import com.github.donkeyrit.telegrambotstepfather.tdlib.TdApi;

public class DefaultHandler implements Client.ResultHandler {
    @Override
    public void onResult(TdApi.Object object) {
        System.out.println(object.toString());
    }
}
