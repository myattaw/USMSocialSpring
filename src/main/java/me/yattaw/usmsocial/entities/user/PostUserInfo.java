package me.yattaw.usmsocial.entities.user;

import lombok.Getter;

@Getter
public class PostUserInfo {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String tagLine;
    
    PostUserInfo(
        int id,
        String firstName,
        String lastName,
        String email,
        String tagLine
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tagLine = tagLine;
    }
}
