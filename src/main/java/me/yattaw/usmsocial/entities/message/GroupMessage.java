package me.yattaw.usmsocial.entities.message;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserGroups;

import java.time.LocalDateTime;

/**
 * Define the properties of a <code>GroupMessage</code>.
 * Group messages are stored in the table <code>usm_social_group_messages</code>.
 * @version 17 April 2024
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usm_social_group_messages")
public class GroupMessage {

    /**
     * id - unique identifier for this message.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * sender - the <code>User</code> who sent this group message.
     */
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false) // Join column for sender
    private User sender;

    /**
     * group - the <code>UserGroups</code> receiving this group message.
     */
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false) // Join column for group
    private UserGroups group;

    /**
     * message - the contents of this group message.
     */
    private String message;

    /**
     * timestamp - the time this group message was sent.
     */
    private LocalDateTime timestamp;

}
