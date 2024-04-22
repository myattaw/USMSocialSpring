package me.yattaw.usmsocial.entities.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a user in the social network system.
 *
 * This class stores information about users, including their personal details,
 * role in the system, profile picture, and verification status.
 *
 * Registering emails are checked for the @maine.edu domain and checked for duplicates.
 *
 * @version 17 April 2024
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usm_social_users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
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

    /**
     * Retrieves the authorities granted to the user.
     *
     * @return A collection of authorities granted to the user.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role.getSimpleGrantedAuthority());
    }

    /**
     * Retrieves the username used to authenticate the user.
     *
     * @return The username used to authenticate the user.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return {@code true} if the user's account is valid (i.e., not expired), {@code false} otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return {@code true} if the user is not locked, {@code false} otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     *
     * @return {@code true} if the user's credentials are valid (i.e., not expired), {@code false} otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return {@code true} if the user is enabled, {@code false} otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Retrieves the base64 encoded profile picture of the user.
     *
     * @return The base64 encoded profile picture of the user.
     */
    public String getBase64ProfilePicture() {
        String encodedString = "";
        if (profilePicture != null && profilePicture.length != 0) {
            encodedString = Base64.getEncoder().encodeToString(profilePicture);
        }
        return encodedString;
    }

    /**
     * Retrieves a simplified representation of user information suitable for a post.
     *
     * @return A simplified representation of user information suitable for a post.
     */
    public PostUserInfo getPostUserInfo() {
        return new PostUserInfo(id, firstName, lastName, email, tagLine, getBase64ProfilePicture());
    }

    /**
     * Retrieves a detailed representation of user information.
     *
     * @return A detailed representation of user information.
     */
    public UserInfo getUserInfo() {
        return new UserInfo(id, firstName, lastName, email, tagLine, bio, getBase64ProfilePicture());
    }

    /**
     * Generates a verification token for the user.
     */
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

    /**
     * Check if two User objects are the same based on the id.
     *
     * @return If object equals.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object) {
            return true;
        }

        if (object != null && object instanceof User)
        {
            return this.id == ((User) object).id;
        }

        return false;
    }
}