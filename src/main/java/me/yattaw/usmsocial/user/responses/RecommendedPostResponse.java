package me.yattaw.usmsocial.user.responses;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yattaw.usmsocial.entities.post.PostComment;
import me.yattaw.usmsocial.entities.post.PostLike;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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