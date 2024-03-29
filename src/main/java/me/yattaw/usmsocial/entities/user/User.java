package me.yattaw.usmsocial.entities.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yattaw.usmsocial.entities.message.DirectMessage;
import me.yattaw.usmsocial.entities.message.GroupMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usm_social_users")
public class User implements UserDetails {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 32)
    private String firstName;

    @Column(length = 32)
    private String lastName;

    private String email;

    @Column(length = 64)
    private String password;

    @Column(length = 50)
    private String tagLine;

    @Column(length = 200)
    private String bio;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Lob
    @Column(name = "profile_picture", columnDefinition="LONGBLOB")
    private byte[] profilePicture;

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private Set<UserGroups> groups;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DirectMessage> sentMessages;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DirectMessage> receivedMessages;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GroupMessage> sentGroupMessages;

    private String verificationToken;

    private boolean verified;

    private LocalDateTime timestamp;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role.getSimpleGrantedAuthority());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public PostUserInfo getPostUserInfo() {
        return new PostUserInfo(id, firstName, lastName, email, tagLine);
    }

    public UserInfo getUserInfo() {
        return new UserInfo(id, firstName, lastName, email, tagLine, bio);
    }

    public void generateVerificationToken() {
        String verificationToken = UUID.randomUUID().toString();
        setVerificationToken(verificationToken);
    }

    /**
     * Validates that the email is associated with the maine.edu domain.
     * Throws an IllegalStateException if the email does not end with "@maine.edu".
     */
    @PrePersist
    @PreUpdate
    public void validateEmail() {
        if (!email.equals("admin") && !email.toLowerCase().endsWith("@maine.edu")) {
            throw new IllegalStateException("Email must be from the maine.edu domain.");
        }
    }

}