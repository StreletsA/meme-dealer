package com.streletsa.memedealer.memestorageservice.model;

import com.mongodb.lang.Nullable;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Memes")
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
    private Boolean approved;
    @Nullable
    private User approver;
    @Indexed(unique = true)
    private String imageHash;

    public Meme(String id, MemeSource source, Long timestamp, Image image){
        this.id = id;
        this.source = source;
        this.timestamp = timestamp;
        this.image = image;
        this.approver = null;
        this.approved = false;

        imageHash = String.valueOf(image.hashCode());
    }

    public void setImage(Image image){
        this.image = image;
        imageHash = String.valueOf(image.hashCode());
    }



}
