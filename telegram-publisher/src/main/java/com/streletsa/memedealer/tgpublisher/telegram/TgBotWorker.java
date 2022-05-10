package com.streletsa.memedealer.tgpublisher.telegram;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import com.streletsa.memedealer.tgpublisher.config.AppConfiguration;


public class TgBotWorker extends TelegramBot{

    private static final String BOT_TOKEN = AppConfiguration.BOT_TOKEN;

    private static TgBotWorker instance;

    private TgBotWorker() {
        super(BOT_TOKEN);
    }

    public static TgBotWorker getInstance(){
        if (instance == null){
            instance = new TgBotWorker();
        }
        return instance;
    }

    public String sendTextMessage(long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        SendResponse response = execute(message);
        return response.message().text();
    }

    public String sendImageMessage(long chatId, byte[] imageBytes){
        SendPhoto image = new SendPhoto(chatId, imageBytes);
        SendResponse response = execute(image);
        return response.message().text();
    }

}
