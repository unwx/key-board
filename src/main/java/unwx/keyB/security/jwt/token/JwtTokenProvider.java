package unwx.keyB.security.jwt.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import unwx.keyB.domains.User;
import unwx.keyB.security.JwtUserDetailsService;
import unwx.keyB.utils.PemUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Component
public class JwtTokenProvider {

    private final String publicPath = "D:\\JavaApps\\keyB\\keys_rsa_2048\\public.pem";
    private final String privatePath = "D:\\JavaApps\\keyB\\keys_rsa_2048\\private8.pem";

    private final RSAPublicKey publicKey = (RSAPublicKey) PemUtils.readPublicKeyFromFile(publicPath, "RSA");
    private final RSAPrivateKey privateKey = (RSAPrivateKey) PemUtils.readPrivateKeyFromFile(privatePath, "RSA");
    private final Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
    
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

    public String getUsername(String token) {
        return JWT.require(algorithm)
                .withIssuer("key-b")
                .build()
                .verify(token).getSubject();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String create(User user) {

        return JWT.create().
                withIssuer("key-b")
                .withSubject(user.getUsername())
                .sign(algorithm);
    }

    public boolean validate(String token) {
        try {
            DecodedJWT jwtDecoded = JWT.require(algorithm)
                    .withIssuer("key-b")
                    .build()
                    .verify(token);

        } catch (JWTVerificationException e) {
            return false;
        }

        return true;
    }
}
