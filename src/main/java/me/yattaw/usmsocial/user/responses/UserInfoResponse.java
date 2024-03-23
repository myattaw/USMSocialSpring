package me.yattaw.usmsocial.user.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yattaw.usmsocial.entities.user.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    
    private UserInfo user;
    private boolean isFollowing;
    private boolean isOwnProfile;

}
