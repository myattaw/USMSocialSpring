package me.yattaw.usmsocial.entities.message;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yattaw.usmsocial.entities.user.User;

import java.time.LocalDateTime;

/**
 * Defines the properties of a <code>DirectMessage</code>.
 * <code>DirectMessages</code> are stored in the table
 * <code>usm_social_direct_messages</code>.
 * @version 17 April 2024
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usm_social_direct_messages")
public class DirectMessage {

    /**
     * id - unique identifier for this direct message.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * sender - the <code>User</code> who sent this direct message.
     */
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    /**
     * receiver - the <code>User</code> receiving this direct message.
     */
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    /**
     * message - the contents of this direct message.
     */
    private String message;

    /**
     * timestamp - the time this direct message was sent.
     */
    private LocalDateTime timestamp;

}