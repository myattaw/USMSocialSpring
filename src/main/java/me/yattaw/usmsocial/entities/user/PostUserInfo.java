package me.yattaw.usmsocial.entities.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUserInfo {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String tagLine;
    private String base64Image;
    
}
