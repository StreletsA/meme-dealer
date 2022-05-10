package com.streletsa.memedealer.tgpublisher.telegram;

import com.streletsa.memedealer.tgpublisher.config.AppConfiguration;

public class MemeDealerBotWorker implements TgChatBot{

    private static final long CHAT_ID = Long.parseLong(AppConfiguration.MEME_DEALER_CHAT_ID);

    private static MemeDealerBotWorker instance;
    private final TgBotWorker botWorker;

    private MemeDealerBotWorker() {
        this.botWorker = TgBotWorker.getInstance();
    }

    public static MemeDealerBotWorker getInstance(){
        if (instance == null){
            instance = new MemeDealerBotWorker();
        }
        return instance;
    }


    @Override
    public void sendTextMessage(String text) {
        botWorker.sendTextMessage(CHAT_ID, text);
    }

    @Override
    public void sendImageMessage(byte[] imageBytes) {
        botWorker.sendImageMessage(CHAT_ID, imageBytes);
    }
}
