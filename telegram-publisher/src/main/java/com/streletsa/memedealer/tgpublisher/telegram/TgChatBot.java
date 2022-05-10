package com.streletsa.memedealer.tgpublisher.telegram;

public interface TgChatBot {

    void sendTextMessage(String text);
    void sendImageMessage(byte[] imageBytes);

}
