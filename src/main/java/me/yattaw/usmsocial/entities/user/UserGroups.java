package me.yattaw.usmsocial.entities.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
/**
 * Represents a group of users in the social network system.
 *
 * This entity class maps to the "usm_social_groups" table in the database.
 * It stores information about user groups, including the group name, members belonging to the group,
 * and a timestamp indicating when the group was created.
 *
 * @version 17 April 2024
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usm_social_groups")
public class UserGroups {

    /**
     * The unique identifier for this group.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer id;

    /**
     * The name of this group.
     */
    private String name;

    /**
     * The members belonging to this group.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usm_social_groups_mapping", // Name of the join table
            joinColumns = @JoinColumn(name = "group_id"), // Column in the join table referring to this entity (UserGroups)
            inverseJoinColumns = @JoinColumn(name = "user_id") // Column in the join table referring to the other entity (User)
    )
    private Set<User> members;

    /**
     * The timestamp indicating when this group was created.
     */
    private LocalDateTime timestamp;

}