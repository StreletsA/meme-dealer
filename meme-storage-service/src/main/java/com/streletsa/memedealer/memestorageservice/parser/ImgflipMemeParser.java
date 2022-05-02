package com.streletsa.memedealer.memestorageservice.parser;

import com.streletsa.memedealer.memestorageservice.model.Image;
import com.streletsa.memedealer.memestorageservice.model.Meme;
import com.streletsa.memedealer.memestorageservice.model.MemeSource;
import com.streletsa.memedealer.memestorageservice.utils.ImageUtils;
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
public class ImgflipMemeParser extends MemeParser{

    private final static MemeSource MEME_SOURCE = MemeSource.IMG_FLIP;
    private final static String URL = "https://imgflip.com/?sort=latest";
    private final static String HTML_IMAGE_CLASS = "base-img";

    @Override
    List<Meme> parseMemes() {
        List<Meme> memeList = new ArrayList<>();

        try {
            Document document = Jsoup.connect(URL).get();
            Elements images = document.getElementsByClass(HTML_IMAGE_CLASS);

            for (Element imageElement : images){

                Optional<Image> imageOptional = ImageUtils.getImageFromElement(imageElement);

                if (imageOptional.isPresent()){
                    Image image = imageOptional.get();
                    Long timestamp = System.currentTimeMillis();

                    Meme meme = new Meme();
                    meme.setSource(MEME_SOURCE);
                    meme.setImage(image);
                    meme.setTimestamp(timestamp);

                    memeList.add(meme);
                }

            }

        } catch (IOException e) {
            log.error("Error in parsing meme. Error -> {}", e.getMessage());
        }

        return memeList;
    }

}
