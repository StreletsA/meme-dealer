package com.streletsa.memedealer.memestorageservice.controller;

import com.streletsa.memedealer.memestorageservice.model.Meme;
import com.streletsa.memedealer.memestorageservice.service.MemeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class MemeController {

    @Autowired
    MemeService memeService;

    @GetMapping("/memes")
    public List<Meme> getAllMemes(@RequestHeader("Token") String token){
        return memeService.getAllMemes(token);
    }

    @GetMapping("/memes")
    public List<Meme> getMemesWithLimitAndTimeOffset(
            @RequestHeader("Token") String token,
            @RequestParam("limit") Integer limit,
            @RequestParam(name = "time_offset", required = false) Long timeOffset
    ){
        List<Meme> memes = new ArrayList<>();
        if (timeOffset == null){
            memes = memeService.getMemesWithLimit(limit);
        }
        else{
            memes = memeService.getMemesWhereTimestampGreaterThan(timeOffset, limit);
        }
        return memes;
    }

    @GetMapping("/memes/unapproved")
    public List<Meme> getUnapprovedMemes(@RequestHeader("Token") String token){
        return memeService.getAllUnapprovedMemes();
    }

    @PostMapping("/memes")
    public void postMemeList(@RequestHeader("Token") String token, @RequestBody List<Meme> memeList){
        memeService.storeMemeList(memeList, token);
    }

    @PutMapping("/memes")
    public void approveMemesByIdList(@RequestHeader("Token") String token, @RequestBody List<String> memeIdList){
        memeService.approveMemeList(memeIdList, token);
    }

}
