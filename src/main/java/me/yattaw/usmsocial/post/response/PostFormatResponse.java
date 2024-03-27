package me.yattaw.usmsocial.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yattaw.usmsocial.entities.user.PostUserInfo;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostFormatResponse {

    private Integer id;
    private String content;
    private PostUserInfo postUserInfo;
    private List<PostCommentResponse> comments;
    private LocalDateTime timestamp;
    private int likeCount;
    private boolean isLiked;

}