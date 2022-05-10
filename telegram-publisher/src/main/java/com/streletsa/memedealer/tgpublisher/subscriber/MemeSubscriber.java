package com.streletsa.memedealer.tgpublisher.subscriber;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import com.streletsa.memedealer.tgpublisher.config.AppConfiguration;
import com.streletsa.memedealer.tgpublisher.model.Meme;
import com.streletsa.memedealer.tgpublisher.telegram.MemeDealerBotWorker;
import com.streletsa.memedealer.tgpublisher.telegram.TgChatBot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeoutException;


public class MemeSubscriber extends Thread{

    private static final String RABBITMQ_PUBLISHER_HOST = AppConfiguration.RABBITMQ_PUBLISHER_HOST;
    private static final String RABBITMQ_QUEUE_NAME = AppConfiguration.RABBITMQ_QUEUE_NAME;

    private final TgChatBot botWorker;
    private final ConnectionFactory connectionFactory;
    private final Queue<Meme> memeQueue;

    public MemeSubscriber(){
        botWorker = MemeDealerBotWorker.getInstance();
        connectionFactory = new ConnectionFactory();
        memeQueue = new ConcurrentLinkedDeque<>();

        connectionFactory.setHost(RABBITMQ_PUBLISHER_HOST);
    }

    @Override
    public void run(){
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()){

            channel.queueDeclare(RABBITMQ_QUEUE_NAME, false, false, false, null);


            while (true){
                waitMemeFromQueue(channel);
                if (!memeQueue.isEmpty()){
                    Meme meme = memeQueue.poll();
                    byte[] imageBytes = meme.getImage().getImageByteArray();
                    botWorker.sendImageMessage(imageBytes);
                }
            }

        } catch (IOException | TimeoutException e) {
            run();
        }
    }

    private void waitMemeFromQueue(Channel channel){
        try {
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String memeJson = new String(delivery.getBody(), StandardCharsets.UTF_8);
                tryAddMemeToQueueFromJson(memeJson);
            };
            channel.basicConsume(RABBITMQ_QUEUE_NAME, true, deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tryAddMemeToQueueFromJson(String memeJson){
        try {
            ObjectMapper mapper = new ObjectMapper();
            Meme meme = mapper.readValue(memeJson, Meme.class);
            memeQueue.add(meme);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
