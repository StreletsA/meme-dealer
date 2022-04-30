package com.streletsa.memedealer.memestorageservice.parser;

import com.streletsa.memedealer.memestorageservice.model.Meme;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class RedditMemeParser extends MemeParser{

    private final static String URL = "https://www.reddit.com/r/memes/";
    private final static String HTML_IMG_CLASS = "_2_tDEnGMLxpM6uOa2kaDB3 ImageBox-image media-element _1XWObl-3b9tPy64oaG6fax";

    @Override
    List<Meme> parseMemes() {
        List<Meme> memeList = new ArrayList<>();

        try {
            Document document = Jsoup.connect(URL).get();
            //Elements images = document.getElementsByClass(HTML_IMG_CLASS);
            Elements images = document.getElementsByAttributeValue("alt", "Post image");

            for (Element image : images){
                Optional<String> imageUrlOptional = getImageUrlFromElement(image);
                if (imageUrlOptional.isPresent()){

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return memeList;
    }

    private Optional<String> downloadImageFromElement(Element element){
        Optional<String> imageUrl = getImageUrlFromElement(element);
        return Optional.empty();
    }

    private Optional<String> getImageUrlFromElement(Element element){
        String url = element.attr("src");
        log.info("URL is -> {}", url);
        return Optional.of(url);
    }

}
