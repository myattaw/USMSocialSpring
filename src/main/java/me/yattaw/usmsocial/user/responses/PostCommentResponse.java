package me.yattaw.usmsocial.user.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentResponse {

    private Long commentId;
    private String content;
    private String commenterFirstName;
    private String commenterLastName;
    private LocalDateTime timestamp;

}
