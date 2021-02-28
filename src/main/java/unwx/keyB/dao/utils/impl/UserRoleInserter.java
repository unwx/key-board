package unwx.keyB.dao.utils.impl;

import org.hibernate.Session;
import unwx.keyB.dao.sql.SqlGenerator;
import unwx.keyB.domains.Role;

import java.util.List;

public class UserRoleInserter {

    private final SqlGenerator sqlGenerator = new SqlGenerator();

    public void setRoles(List<Role> roles, Long userId, Session session) {
        StringBuilder sb = new StringBuilder();
        roles.forEach((r) -> sb.append(r.getAuthority()).append(" "));
        sb.deleteCharAt(sb.length() - 1);

        session.createSQLQuery(
                "INSERT INTO user_role (`user_id`, `roles`)" +
                        " VALUE ('" + userId + "'," + "'" + sb.toString() + "'" + ");")
        .executeUpdate();
    }
}
