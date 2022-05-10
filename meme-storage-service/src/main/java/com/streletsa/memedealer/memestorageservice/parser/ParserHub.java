package com.streletsa.memedealer.memestorageservice.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class ParserHub {

    @Autowired
    ImgflipMemeParser memeParser;

    @PostConstruct
    public void init(){
        memeParser.start();
    }

}
