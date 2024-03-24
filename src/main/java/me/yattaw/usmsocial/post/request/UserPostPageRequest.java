package me.yattaw.usmsocial.post.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPostPageRequest {

    private int pageNumber;
    private int pageSize;
}
