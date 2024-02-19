package me.yattaw.usmsocial.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedPostResponse {

    private Integer id;
    private String content;
    private String postersFirstName;
    private String postersLastName;
    private List<PostCommentResponse> comments;
    private LocalDateTime timestamp;
    private int likeCount;

}