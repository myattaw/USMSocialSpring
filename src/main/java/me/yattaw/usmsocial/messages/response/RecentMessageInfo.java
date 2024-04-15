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
public class RecentMessageInfo {

    private Integer userId;
    private String firstName;
    private String lastName;
    private String tagLine;
    private String base64Image;
    private String lastMessage;
    private LocalDateTime timestamp;

}