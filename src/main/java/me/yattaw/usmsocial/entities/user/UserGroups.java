package me.yattaw.usmsocial.entities.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usm_social_groups")
public class UserGroups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usm_social_groups_mapping", // Name of the join table
            joinColumns = @JoinColumn(name = "group_id"), // Column in the join table referring to this entity (UserGroups)
            inverseJoinColumns = @JoinColumn(name = "user_id") // Column in the join table referring to the other entity (User)
    )
    private Set<User> members;


    private LocalDateTime timestamp;

}