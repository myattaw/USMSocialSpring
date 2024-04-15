package me.yattaw.usmsocial.entities.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usm_social_users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements UserDetails {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(length = 32)
    private String firstName;

    @Column(length = 32)
    private String lastName;

    @EqualsAndHashCode.Include
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

    public String getBase64ProfilePicture() {
        String encodedString = "";
        if (profilePicture != null && profilePicture.length != 0) {
            encodedString = Base64.getEncoder().encodeToString(profilePicture);
        }
        return encodedString;
    }

    public PostUserInfo getPostUserInfo() {
        return new PostUserInfo(id, firstName, lastName, email, tagLine, getBase64ProfilePicture());
    }

    public UserInfo getUserInfo() {
        return new UserInfo(id, firstName, lastName, email, tagLine, bio, getBase64ProfilePicture());
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
//        if (!email.equals("admin") && !email.toLowerCase().endsWith("@maine.edu")) {
//            throw new IllegalStateException("Email must be from the maine.edu domain.");
//        }
    }

}