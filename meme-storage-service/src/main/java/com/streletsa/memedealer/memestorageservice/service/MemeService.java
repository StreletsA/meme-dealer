package com.streletsa.memedealer.memestorageservice.service;

import com.streletsa.memedealer.memestorageservice.model.Meme;
import com.streletsa.memedealer.memestorageservice.repository.MemeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MemeService {

    @Autowired
    MemeRepository memeRepository;

    public void storeMeme(Meme meme){
        try {
            memeRepository.insert(meme);
        } catch (Exception e){
            log.error("Meme insertion error -> {}", e.getMessage());
        }

    }

    public List<Meme> getAllMemes(){
        List<Meme> memeList = new ArrayList<>();

        try {
            memeList = memeRepository.findAll();
        } catch (Exception e){
            log.error("Meme list reading error -> {}", e.getMessage());
        }

        return memeList;
    }

}
