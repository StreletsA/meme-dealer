package com.streletsa.memedealer.memestorageservice.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ParserHub {

    @Autowired
    ImgflipMemeParser memeParser;

    @PostConstruct
    public void init(){
        memeParser.start();
    }

}
