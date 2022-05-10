package com.streletsa.memedealer.tgpublisher.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String id;
    private String name;
    private String token;

}
