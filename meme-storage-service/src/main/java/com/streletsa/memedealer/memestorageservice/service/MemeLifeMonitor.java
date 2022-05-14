package com.streletsa.memedealer.memestorageservice.service;

import com.streletsa.memedealer.memestorageservice.config.ConstantsConfig;
import com.streletsa.memedealer.memestorageservice.model.Meme;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MemeLifeMonitor extends Thread{

    private static final Long TIME_FOR_MEME_APPROVING = Long.parseLong(ConstantsConfig.TIME_FOR_MEME_APPROVING);

    private final MemeService memeService;


    public MemeLifeMonitor(MemeService memeService){
        this.memeService = memeService;
    }

    @Override
    public void run(){
        startMonitor();
    }

    private void startMonitor(){
        while (true) {
            deleteMemesUnapprovedLongTime();
            trySleepThread();
        }
    }

    private void deleteMemesUnapprovedLongTime(){
        List<Meme> memesUnapprovedLongTime = getMemesUnapprovedLongTime();
        memeService.deleteMemeList(memesUnapprovedLongTime);
    }

    private List<Meme> getMemesUnapprovedLongTime(){
        List<Meme> allUnapprovedMemes = memeService.getAllUnapprovedMemes();
        long timestampNow = System.currentTimeMillis();

        return allUnapprovedMemes
                .stream()
                .filter(meme -> timestampNow - meme.getTimestamp() > TIME_FOR_MEME_APPROVING)
                .collect(Collectors.toList());
    }

    private void trySleepThread(){
        try {
            Thread.sleep(TIME_FOR_MEME_APPROVING);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
