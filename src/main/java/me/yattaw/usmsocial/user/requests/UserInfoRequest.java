package me.yattaw.usmsocial.user.requests;

import me.yattaw.usmsocial.entities.user.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequest {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String tagLine;
    private String bio;
}
