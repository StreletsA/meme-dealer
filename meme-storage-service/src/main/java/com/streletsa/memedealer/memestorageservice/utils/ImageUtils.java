package com.streletsa.memedealer.memestorageservice.utils;

import com.streletsa.memedealer.memestorageservice.model.Image;
import com.streletsa.memedealer.memestorageservice.model.ImageExtension;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

@Slf4j
public class ImageUtils {

    public static Optional<Image> getImageFromElement(Element element){
        Optional<Image> imageOptional = Optional.empty();

        String imageExtensionString = null;
        byte[] imageByteArray = null;

        Optional<String> imageUrlStringOptional = getImageUrlAsStringFromElement(element);
        if (imageUrlStringOptional.isPresent()){
            String imageUrlString = imageUrlStringOptional.get();
            imageExtensionString = deriveImageExtensionFromUrlString(imageUrlString);
            log.info("Image URL -> {}", imageUrlString);
        }

        Optional<byte[]> imageByteArrayOptional = downloadImageFromElementAsByteArray(element);
        if (imageByteArrayOptional.isPresent()){
            imageByteArray = imageByteArrayOptional.get();
        }

        if (imageExtensionString != null && imageByteArray != null){
            try {
                ImageExtension imageExtension = ImageExtension.valueOf(imageExtensionString.toUpperCase(Locale.ROOT));
                Image image = new Image();
                image.setImageByteArray(imageByteArray);
                image.setExtension(imageExtension);
                image.setUrl(imageUrlStringOptional.get());
                imageOptional = Optional.of(image);
            } catch (IllegalArgumentException e){
                log.error("There is no value of extension -> {}", imageExtensionString.toUpperCase(Locale.ROOT));
            }

        }

        return imageOptional;
    }

    public static void saveImage(Image image) {
        try {
            String imageExtension = image.getExtension().name().toLowerCase();
            String imageName = String.valueOf(System.currentTimeMillis());
            String imageFileName = imageName + '.' + imageExtension;
            File file = new File("/home/andrey/Documents/" + imageFileName);
            if (file.createNewFile()){
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(image.getImageByteArray());
            }
            else{
                log.info("File {} not created", imageFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Optional<byte[]> downloadImageFromElementAsByteArray(Element element){
        Optional<byte[]> imageByteArrayOptional = Optional.empty();
        Optional<URL> imageUrlOptional = getImageUrlFromElement(element);

        if (imageUrlOptional.isPresent()){
            URL imageUrl = imageUrlOptional.get();
            try(InputStream is = imageUrl.openStream ();) {
                byte[] imageBytes = is.readAllBytes();
                imageByteArrayOptional = Optional.of(imageBytes);
            }
            catch (IOException e) {
                log.error ("Failed while reading bytes from {}: {}", imageUrl.toExternalForm(), e.getMessage());
            }
        }

        return imageByteArrayOptional;
    }

    public static Optional<URL> getImageUrlFromElement(Element element){

        Optional<URL> imageUrlOptional = Optional.empty();
        Optional<String> urlStringOptional = getImageUrlAsStringFromElement(element);

        if (urlStringOptional.isEmpty()){
            return imageUrlOptional;
        }

        try {
            String urlString = urlStringOptional.get();
            urlString = correctUrl(urlString);
            URL imageUrl = new URL(urlString);
            imageUrlOptional = Optional.of(imageUrl);
        } catch (MalformedURLException e) {
            log.error("Can't to derive url from string. Error message -> {}", e.getMessage());
        }

        return imageUrlOptional;
    }

    public static String correctUrl(String url){
        if (isValidUrl(url)){
            return url;
        }
        String prefix = "";
        if (!url.contains("http://") && !url.contains("https://")){
            if (!url.contains("://")){
                if (!url.contains("//")){
                    prefix = "http://";
                }
                else{
                    prefix = "http:";
                }
            }
            else {
                prefix += "http";
            }
        }

        return prefix + url;
    }

    public static boolean isValidUrl(String urlToValidate){
        try{
            URL url = new URL(urlToValidate);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static Optional<String> getImageUrlAsStringFromElement(Element element){
        Optional<String> urlOptional = Optional.empty();
        String urlString = element.attr("src");

        if (urlString != null && urlString.length() > 0){
            urlOptional = Optional.of(urlString);
        }

        return urlOptional;
    }

    public static String deriveImageExtensionFromUrlString(String imageUrl){
        StringBuilder extension = new StringBuilder();
        for (int i = imageUrl.length() - 1; i >= 0; i--){
            char ch = imageUrl.charAt(i);
            if (ch == '.'){
                break;
            }
            extension.insert(0, ch);
        }
        return extension.toString();
    }

}
