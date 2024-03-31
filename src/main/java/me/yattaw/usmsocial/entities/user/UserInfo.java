package me.yattaw.usmsocial.entities.user;

import lombok.Getter;

@Getter
public class UserInfo extends PostUserInfo {

    private String bio;
    
    public UserInfo(
        int id,
        String firstName,
        String lastName,
        String email,
        String tagLine,
        String bio,
        String imageBase64
    ) {
        super(id, firstName, lastName, email, tagLine, imageBase64);
        this.bio = bio;
    }
}
