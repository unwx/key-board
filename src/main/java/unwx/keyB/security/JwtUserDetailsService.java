package unwx.keyB.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import unwx.keyB.dao.UserDao;
import unwx.keyB.dao.sql.entities.DatabaseTable;
import unwx.keyB.dao.sql.entities.SqlField;
import unwx.keyB.dao.sql.entities.SqlTableRequest;
import unwx.keyB.domains.User;
import unwx.keyB.security.jwt.user.JwtUserFactory;

import java.util.Collections;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserDao userDao = new UserDao();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<String> columns = new User.Columns()
                .username()
                .id()
                .email()
                .active()
                .accessTokenExpiration()
                .refreshTokenExpiration()
                .password()
                .get();
        SqlTableRequest request = new SqlTableRequest(
                DatabaseTable.USER_ROLE,
                Collections.singletonList("roles"),
                true);

        User user = userDao.readLazy(columns, new SqlField(username, "username"));

        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }
        User userWithLinkedAttr = userDao.readLinkedEntity(user.getId(), request);
        user.setRoles(userWithLinkedAttr.getRoles());

        return JwtUserFactory.create(user);
    }
}
