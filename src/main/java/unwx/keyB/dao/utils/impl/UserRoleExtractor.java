package unwx.keyB.dao.utils.impl;

import org.jetbrains.annotations.Nullable;
import unwx.keyB.domains.Role;

import java.util.LinkedList;
import java.util.List;

public class UserRoleExtractor {

    public static List<Role> extractRolesFromObject(Object obj) {
        String value = (String) obj;
        List<Role> roles = new LinkedList<>();
        for (String s : value.split(" ")) {
            roles.add(getRoleFromString(s));
        }
        return roles;
    }

    public static List<Role> extractRolesFromObject(Object[] obj) {
        String value = (String) obj[0];
        List<Role> roles = new LinkedList<>();
        for (String s : value.split(" ")) {
            roles.add(getRoleFromString(s));
        }
        return roles;
    }

    @Nullable
    private static Role getRoleFromString(String s) {
        if ("USER".equals(s)) {
            return Role.USER;
        }
        if ("MODERATOR".equals(s)){
            return Role.MODERATOR;
        }
        if ("ADMIN".equals(s)){
            return Role.ADMIN;
        }
        return null;
    }

}
