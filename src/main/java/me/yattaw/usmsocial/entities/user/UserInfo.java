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
        String bio
    ) {
        super(id, firstName, lastName, email, tagLine);
        this.bio = bio;
    }
}
