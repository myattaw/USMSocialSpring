package me.yattaw.usmsocial.user.responses;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowListResponse {
    Page<UserSearch> userFollowList;
}
