package me.yattaw.usmsocial.entities.message;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserGroups;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usm_social_group_messages")
public class GroupMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false) // Join column for sender
    private User sender;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false) // Join column for group
    private UserGroups group;

    private String message;

    private LocalDateTime timestamp;

}
