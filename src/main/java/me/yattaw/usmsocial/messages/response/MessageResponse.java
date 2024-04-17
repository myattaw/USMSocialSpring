package me.yattaw.usmsocial.messages.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private Integer userId;
    private String content;
    private String firstName;
    private String lastName;
    private LocalDateTime timestamp;

}