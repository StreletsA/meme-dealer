package com.streletsa.memedealer.memestorageservice.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.streletsa.memedealer.memestorageservice.config.ConstantsConfig;
import com.streletsa.memedealer.memestorageservice.model.Meme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class MemePublisher extends Thread{

    private static final String rabbitmqHost = ConstantsConfig.RABBITMQ_HOST;
    private static final String rabbitmqQueueName = ConstantsConfig.RABBITMQ_QUEUE_NAME;

    private final ConnectionFactory connectionFactory;
    private final ConcurrentLinkedQueue<Meme> memeQueue;

    public MemePublisher(){
        connectionFactory = new ConnectionFactory();
        memeQueue = new ConcurrentLinkedQueue<>();
    }

    @PostConstruct
    private void init(){
        connectionFactory.setHost(rabbitmqHost);
    }

    @Override
    public void run(){
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()){
            channel.queueDeclare(rabbitmqQueueName, false, false, false, null);
            while (true){

                while (!memeQueue.isEmpty()){

                    Meme meme = memeQueue.poll();
                    publishMemeIntoRabbitmqQueue(channel, meme);

                }
            }
        } catch (IOException | TimeoutException e) {
            log.error("Connection error. Host -> {}. Error -> {}", rabbitmqHost, e.getLocalizedMessage());
            log.error("Reconnecting...");
            trySleepThread(1000);
            run();
        }
    }

    private void publishMemeIntoRabbitmqQueue(Channel channel, Meme meme){
        ObjectMapper mapper = new ObjectMapper();

        try {
            String memeJson = mapper.writeValueAsString(meme);
            channel.basicPublish(
                    "",
                    rabbitmqQueueName,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    memeJson.getBytes(StandardCharsets.UTF_8)
            );
            log.info("Meme published!");
        } catch (IOException e) {
            log.error("Meme publishing ended with error -> {}", e.getMessage());
        }
    }

    public void publishMeme(Meme meme){
        memeQueue.add(meme);
    }

    private void trySleepThread(long milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
