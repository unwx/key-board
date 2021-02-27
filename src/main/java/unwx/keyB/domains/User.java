package unwx.keyB.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import unwx.keyB.dao.sql.entities.SqlAttributesExtractor;
import unwx.keyB.dao.sql.entities.SqlField;
import unwx.keyB.dao.sql.entities.SqlQueryAttributes;

import javax.persistence.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@SuppressWarnings("unused")
public class User implements SqlAttributesExtractor {

    @Id
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private Boolean active;

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
    private List<Role> roles;

    @OneToMany(mappedBy = "author", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "author", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Article> articles;

    public User(String username,
                String password,
                Boolean active,
                List<Role> roles,
                String email,
                String accessTokenExpiration,
                String refreshTokenExpiration,
                List<Comment> comments,
                List<Article> articles) {
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

    public User(Long id,
                String username,
                String password,
                Boolean active,
                String email,
                String avatarName,
                String accessTokenExpiration,
                String refreshTokenExpiration,
                String accessToken,
                String refreshToken,
                List<Role> roles,
                List<Comment> comments,
                List<Article> articles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active;
        this.email = email;
        this.avatarName = avatarName;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.roles = roles;
        this.comments = comments;
        this.articles = articles;
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

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }


    /**
     * @transient accessToken,
     * refreshToken.
     */
    @Override
    public SqlQueryAttributes getFields() {
        List<SqlField> fields = new ArrayList<>() {
            @Serial
            private static final long serialVersionUID = 8002889362110590448L;

            {
                add(new SqlField(id, "id"));
                add(new SqlField(username, "username"));
                add(new SqlField(password, "password"));
                add(new SqlField(email, "email"));
                add(new SqlField(avatarName, "avatar_name"));
                add(new SqlField(accessTokenExpiration, "access_expiration"));
                add(new SqlField(refreshTokenExpiration, "refresh_expiration"));
            }
        };
        return new SqlQueryAttributes(fields, new SqlField(id, "id"));
    }

    @Override
    public SqlField getPrimaryKey() {
        return new SqlField(id, "id");
    }

    @Override
    public SqlField getSecondUniqueKey() {
        return new SqlField(username, "username");
    }

    public String getSecondKey() {
        return username;
    }

    public static class Builder {
        private final User user;

        public Builder() {
            user = new User();
        }

        public Builder id(Long id) {
            user.id = id;
            return this;
        }

        public Builder username(String username) {
            user.username = username;
            return this;
        }

        public Builder password(String password) {
            user.password = password;
            return this;
        }

        public Builder active(Boolean active) {
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

        public Builder roles(List<Role> roles) {
            user.roles = roles;
            return this;
        }

        public Builder comments(List<Comment> comments) {
            user.comments = comments;
            return this;
        }

        public Builder articles(List<Article> articles) {
            user.articles = articles;
            return this;
        }

        public User build() {
            return user;
        }
    }

    public static class Columns {

        private final List<String> columns;

        public Columns() {
            columns = new LinkedList<>();
        }

        public Columns id() {
            columns.add("id");
            return this;
        }

        public Columns username() {
            columns.add("username");
            return this;
        }

        public Columns password() {
            columns.add("password");
            return this;
        }

        public Columns active() {
            columns.add("active");
            return this;
        }

        public Columns email() {
            columns.add("email");
            return this;
        }

        public Columns avatarName() {
            columns.add("avatar_name");
            return this;
        }

        public Columns accessTokenExpiration() {
            columns.add("access_expiration");
            return this;
        }

        public Columns refreshTokenExpiration() {
            columns.add("refresh_expiration");
            return this;
        }

        public List<String> get() {
            return columns;
        }
    }
}
