package me.yattaw.usmsocial.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Page<PostFormatResponse> pageResult;
    private LocalDateTime dateTimeFetch;
}
