package me.yattaw.usmsocial.entities.user;

import lombok.Getter;

@Getter
public class PostUserInfo {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String tagLine;
    private String base64Image;
    
    PostUserInfo(
        int id,
        String firstName,
        String lastName,
        String email,
        String tagLine,
        String base64Image
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tagLine = tagLine;
        this.base64Image = base64Image;
    }
}
