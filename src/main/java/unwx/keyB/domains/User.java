package unwx.keyB.domains;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
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

    public User(String username,
                String password,
                boolean active,
                Set<Role> roles,
                String email,
                String accessTokenExpiration,
                String refreshTokenExpiration) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.roles = roles;
        this.email = email;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
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



}
