package unwx.keyB.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import unwx.keyB.domains.Role;
import unwx.keyB.domains.User;
import unwx.keyB.exceptions.rest.exceptions.BadRequestException;
import unwx.keyB.exceptions.rest.exceptions.ResourceNotFoundException;
import unwx.keyB.repositories.UserRepository;
import unwx.keyB.security.jwt.token.JWTokenData;
import unwx.keyB.security.jwt.token.JwtTokenProvider;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("not found"));
    }

    public void delete(User user) {
        userRepository.delete(user);
    }


    public User register(User user, JwtTokenProvider provider) {

        User isDuplicate = userRepository.findByUsername(user.getUsername());

        if (isDuplicate == null) {
            user.setRoles(Collections.singleton(Role.USER));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setActive(true);
            return userRepository.save(updateUserToken(user, provider));
        }
        else throw new BadRequestException("User with this username already registered.");
    }

    public User refreshTokens(User user, JwtTokenProvider provider) {
        return userRepository.save(updateUserToken(user, provider));
    }

    private User updateUserToken(User user, JwtTokenProvider provider) {
        JWTokenData accessTokenData = provider.createAccess(user);
        JWTokenData refreshTokenData = provider.createRefresh(user);
        user.setAccessTokenExpiration(String.valueOf(accessTokenData.getExpirationAtMillis()));
        user.setRefreshTokenExpiration(String.valueOf(refreshTokenData.getExpirationAtMillis()));
        user.setAccessToken(accessTokenData.getToken());
        user.setRefreshToken(refreshTokenData.getToken());
        return user;
    }


}
