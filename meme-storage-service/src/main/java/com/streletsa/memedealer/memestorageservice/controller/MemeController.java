package com.streletsa.memedealer.memestorageservice.controller;

import com.streletsa.memedealer.memestorageservice.model.Meme;
import com.streletsa.memedealer.memestorageservice.service.MemeService;
import com.streletsa.memedealer.memestorageservice.service.UserService;
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
    @Autowired
    UserService userService;

    @GetMapping("/memes")
    public List<Meme> getMemesWithLimitAndTimeOffset(
            @RequestHeader("Token") String token,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "time_offset", required = false) Long timeOffset
    ){
        List<Meme> memes = new ArrayList<>();
        if (userService.isValidToken(token)){
            if (limit == null){
                memes = memeService.getAllMemes(token);
            }
            else if (timeOffset == null){
                memes = memeService.getMemesWithLimit(limit);
            }
            else{
                memes = memeService.getMemesWhereTimestampGreaterThan(timeOffset, limit);
            }
        }
        return memes;
    }

    @GetMapping("/memes/unapproved")
    public List<Meme> getUnapprovedMemes(
            @RequestHeader("Token") String token,
            @RequestParam(name = "limit", required = false) Integer limit
    ){
        List<Meme> memes = new ArrayList<>();
        if (userService.isValidToken(token)){
            if (limit == null){
                memes = memeService.getUnapprovedMemesWithLimit(limit);
            }
            else {
                memes = memeService.getAllUnapprovedMemes();
            }
        }
        return memes;
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
