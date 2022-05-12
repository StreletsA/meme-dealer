package com.streletsa.memedealer.memestorageservice.model;

import lombok.*;

import java.util.Arrays;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Image {

    private String url;
    private byte[] imageByteArray;
    private ImageExtension extension;

    public int hashCode(){
        return Arrays.hashCode(imageByteArray);
    }

    public boolean equals(Object o){
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Image image = (Image) o;
        return hashCode() == o.hashCode();

    }
}
