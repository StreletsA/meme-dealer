package com.streletsa.memedealer.tgpublisher.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Meme {

    private String id;
    private MemeSource source;
    private Long timestamp;
    private Image image;
    private Boolean approved;
    private User approver;
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
