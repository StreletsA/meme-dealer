package com.streletsa.memedealer.memestorageservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "MemesCollection")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Meme {

    @Id
    private String id;
    private MemeSource source;
    private Long timestamp;
    private Image image;
    @Indexed(unique = true)
    private String imageHash;

    public Meme(String id, MemeSource source, Long timestamp, Image image){
        this.id = id;
        this.source = source;
        this.timestamp = timestamp;
        this.image = image;

        imageHash = String.valueOf(image.hashCode());
    }

    public void setImage(Image image){
        this.image = image;
        imageHash = String.valueOf(image.hashCode());
    }



}
