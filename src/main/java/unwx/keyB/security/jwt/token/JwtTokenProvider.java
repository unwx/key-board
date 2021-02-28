package unwx.keyB.security.jwt.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import unwx.keyB.domains.User;
import unwx.keyB.dto.ClaimsDto;
import unwx.keyB.security.JwtUserDetailsService;
import unwx.keyB.security.jwt.JwtAuthenticationException;
import unwx.keyB.security.jwt.user.JwtUser;
import unwx.keyB.utils.PemUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String publicPath = "D:\\JavaApps\\keyB\\keys_rsa_2048\\public.pem";
    private final String privatePath = "D:\\JavaApps\\keyB\\keys_rsa_2048\\private8.pem";

    private final RSAPublicKey publicKey = (RSAPublicKey) PemUtils.readPublicKeyFromFile(publicPath, "RSA");
    private final RSAPrivateKey privateKey = (RSAPrivateKey) PemUtils.readPrivateKeyFromFile(privatePath, "RSA");
    private final Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
    private final long accessExpirationTime = 3600000L; // 1 hour
    private final long refreshExpirationTime = 31536000000L; // 1 year

    private final JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    public JwtTokenProvider(JwtUserDetailsService jwtUserDetailsService) throws IOException {
        this.jwtUserDetailsService = jwtUserDetailsService;
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveToken(String token) {
        if (token != null && token.startsWith("Bearer_")) {
            return token.substring(7);
        }
        return token;
    }

    public String getUsername(String token) {
        try {
            return JWT.require(algorithm)
                    .withIssuer("key-b")
                    .build()
                    .verify(token).getSubject();
        }catch (Exception e) {
            throw new JwtAuthenticationException();
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public JWTokenData createAccess(User user) {
        long expiration = new Date(getCurrentTimeAtMillis() + accessExpirationTime).getTime();
        String token = JWT.create().
                withIssuer("key-b")
                .withClaim("type", "access")
                .withSubject(user.getUsername())
                .withClaim("expiration", String.valueOf(expiration))
                .sign(algorithm);
        return new JWTokenData(token, expiration);
    }

    public JWTokenData createRefresh(User user) {
        long expiration = new Date(getCurrentTimeAtMillis() + refreshExpirationTime).getTime();
        String token = JWT.create().
                withIssuer("key-b")
                .withClaim("type", "refresh")
                .withSubject(user.getUsername())
                .withClaim("expiration", String.valueOf(expiration))
                .sign(algorithm);
        return new JWTokenData(token, expiration);
    }

    public ImmutablePair<JwtStatus, JwtUser> validate(String token) {
        try {
            DecodedJWT jwtDecoded = JWT.require(algorithm)
                    .withIssuer("key-b")
                    .build()
                    .verify(token);
            long dateAtMillis = Long.parseLong(jwtDecoded.getClaim("expiration").asString());

            if (getCurrentTimeAtMillis() > dateAtMillis)
                return new ImmutablePair<>(JwtStatus.EXPIRED, null);

            JwtUser user = (JwtUser) jwtUserDetailsService.loadUserByUsername(jwtDecoded.getSubject());
            if (!(isTokenActual(dateAtMillis, Long.parseLong(user.getAccessTokenExpiration()))
            || isTokenActual(dateAtMillis, Long.parseLong(user.getRefreshTokenExpiration())))) {
                return new ImmutablePair<>(JwtStatus.INVALID, null);
            }
            return new ImmutablePair<>(JwtStatus.VALID, user);
        } catch (JWTVerificationException e) {
            return new ImmutablePair<>(JwtStatus.INVALID, null);
        }
    }

    public ClaimsDto getClaims(@NotNull String token) {
        DecodedJWT jwtDecoded = JWT.require(algorithm)
                .withIssuer("key-b")
                .build()
                .verify(token);

        return new ClaimsDto(jwtDecoded.getClaims());
    }

    private long getCurrentTimeAtMillis() {
        return LocalDateTime.now()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    private boolean isTokenActual(long tokenExpiration, long actualTokenExpiration) {
        return tokenExpiration == actualTokenExpiration;
    }

}
