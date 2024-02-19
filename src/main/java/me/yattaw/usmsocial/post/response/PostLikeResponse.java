package me.yattaw.usmsocial.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeResponse {

    private Long likeID;
    private String likerFirstName;
    private String likerLastName;
    private LocalDateTime timestamp;

}
