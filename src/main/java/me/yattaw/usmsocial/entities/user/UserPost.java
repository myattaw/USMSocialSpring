package me.yattaw.usmsocial.entities.user;

import jakarta.persistence.*;
import lombok.*;
import me.yattaw.usmsocial.entities.post.PostLike;
import me.yattaw.usmsocial.entities.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "likes")
@Table(name = "usm_social_posts")
public class UserPost {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 280)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime timestamp;

    @OneToMany(mappedBy = "post")
    private Set<PostLike> likes = new HashSet<>();

    @Transient
    private int likeCount; // Transient field for dynamic calculation

    public void updateLikeCount() {
        this.likeCount = likes.size();
    }

    // Method to add a like
    public void addLike(PostLike like) {
        likes.add(like);
        updateLikeCount();
    }

    // Method to remove a like
    public void removeLike(PostLike like) {
        likes.remove(like);
        updateLikeCount();
    }

}
