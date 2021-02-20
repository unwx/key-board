package unwx.keyB.domains;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
@SuppressWarnings("unused")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private boolean active;

    @Column(name = "email")
    private String email;

    @Column(name = "avatar_name")
    @JsonProperty("avatar_name")
    private String avatarName;

    /*
     * if a token is stolen, an attacker can use it forever,
     * so this identifier (token life length) will be checked against the user's current token
     * so that when a new token is created, the old one is irrelevant,
     * despite its validity
     */
    @Column(name = "access_expiration", length = 30)
    private String accessTokenExpiration;
    @Column(name = "refresh_expiration", length = 30)
    private String refreshTokenExpiration;

    @Transient
    private String accessToken;
    @Transient
    private String refreshToken;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToMany(mappedBy = "author", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "author", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Article> articles;

    public User(String username,
                String password,
                boolean active,
                Set<Role> roles,
                String email,
                String accessTokenExpiration,
                String refreshTokenExpiration,
                Set<Comment> comments,
                Set<Article> articles) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.roles = roles;
        this.email = email;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.comments = comments;
        this.articles = articles;
    }

    public User() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public void setAccessTokenExpiration(String accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public String getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public void setRefreshTokenExpiration(String refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    // json view but no magic
    public static class Builder {
        private final User user;

        public Builder() {
            user = new User();
        }

        public Builder username(String username) {
            user.username = username;
            return this;
        }

        public Builder password(String password) {
            user.password = password;
            return this;
        }

        public Builder active(boolean active) {
            user.active = active;
            return this;
        }

        public Builder email(String email) {
            user.email = email;
            return this;
        }

        public Builder avatarName(String avatarName) {
            user.avatarName = avatarName;
            return this;
        }

        public Builder accessTokenExpiration(String accessTokenExpiration) {
            user.accessTokenExpiration = accessTokenExpiration;
            return this;
        }

        public Builder refreshTokenExpiration(String refreshTokenExpiration) {
            user.refreshTokenExpiration = refreshTokenExpiration;
            return this;
        }

        public Builder accessToken(String accessToken) {
            user.accessToken = accessToken;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            user.refreshToken = refreshToken;
            return this;
        }

        public Builder roles(Set<Role> roles) {
            user.roles = roles;
            return this;
        }

        public Builder comments(Set<Comment> comments) {
            user.comments = comments;
            return this;
        }

        public Builder articles(Set<Article> articles) {
            user.articles = articles;
            return this;
        }

        public User build() {
            return user;
        }
    }
}
