package me.yattaw.usmsocial.post.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostNewResponse {
    private List<PostFormatResponse> posts;
    private LocalDateTime serverDateTime;
}