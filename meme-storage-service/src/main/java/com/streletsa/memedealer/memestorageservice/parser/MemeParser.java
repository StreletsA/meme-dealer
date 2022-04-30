package com.streletsa.memedealer.memestorageservice.parser;

import com.streletsa.memedealer.memestorageservice.model.Meme;
import com.streletsa.memedealer.memestorageservice.service.MemeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public abstract class MemeParser extends Thread {

    @Autowired
    MemeService memeService;

    private final static Long PARSING_TIMEOUT_MILLISECONDS = 60_000L * 60;

    @Override
    public void run(){

        while (true) {
            List<Meme> memeList = parseMemes();
            for (Meme meme : memeList) {
                memeService.storeMeme(meme);
            }
            trySleepThread();
        }

    }

    private void trySleepThread(){
        try {
            Thread.sleep(PARSING_TIMEOUT_MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("MemeParser can't sleep -> {}", e.getMessage());
        }
    }

    abstract List<Meme> parseMemes();
}
