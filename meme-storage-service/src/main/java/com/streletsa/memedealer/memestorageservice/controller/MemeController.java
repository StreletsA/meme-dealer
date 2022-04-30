package com.streletsa.memedealer.memestorageservice.controller;

import com.streletsa.memedealer.memestorageservice.model.Meme;
import com.streletsa.memedealer.memestorageservice.service.MemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemeController {

    @Autowired
    MemeService memeService;

    @GetMapping("/memes/all")
    public List<Meme> getAllMemes(){
        return  memeService.getAllMemes();
    }

}
