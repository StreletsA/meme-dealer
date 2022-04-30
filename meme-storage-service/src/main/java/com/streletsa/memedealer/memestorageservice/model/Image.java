package com.streletsa.memedealer.memestorageservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Image {

    @Id
    private String id;
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
