package unwx.keyB.security.jwt.token;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import unwx.keyB.dto.ClaimsDto;
import unwx.keyB.security.jwt.user.JwtUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);

        if (token != null) {
            ImmutablePair<JwtStatus, JwtUser> jwtValidation = jwtTokenProvider.validate(token);
            if (jwtValidation.left == JwtStatus.VALID) {
                ClaimsDto claims = jwtTokenProvider.getClaims(token);
                String type = claims.getClaims().get("type").asString();
                if (type.equals("access") && !((HttpServletRequest) servletRequest).getRequestURI().equals("/api/auth/refresh"))
                    processAccessToken(jwtValidation.right);
                if (type.equals("refresh") && ((HttpServletRequest) servletRequest).getRequestURI().equals("/api/auth/refresh"))
                    processRefreshToken(servletRequest, claims);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void processAccessToken(JwtUser user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void processRefreshToken(ServletRequest servletRequest, ClaimsDto claims) {
        servletRequest.setAttribute("claims", claims);
    }
}
