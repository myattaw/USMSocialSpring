package me.yattaw.usmsocial.entities.user;

import lombok.Getter;

@Getter
public class UserInfo extends PostUserInfo {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String tagLine;
    private String bio;
    
    UserInfo(
        int id,
        String firstName,
        String lastName,
        String email,
        String tagLine,
        String bio
    ) {
        super(id, firstName, lastName, email, tagLine);
        this.bio = bio;
    }
}
