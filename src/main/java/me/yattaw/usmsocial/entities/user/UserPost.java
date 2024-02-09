package me.yattaw.usmsocial.entities.user;

import jakarta.persistence.*;
import lombok.*;
import me.yattaw.usmsocial.entities.post.PostComment;
import me.yattaw.usmsocial.entities.post.PostLike;
import me.yattaw.usmsocial.entities.user.User;

import java.time.LocalDateTime;
import java.util.*;

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

    @OneToMany(mappedBy = "post")
    private Set<PostLike> likes = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> comments = new ArrayList<>();

    private LocalDateTime timestamp;

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
