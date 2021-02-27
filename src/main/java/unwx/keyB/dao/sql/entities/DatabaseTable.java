package unwx.keyB.dao.sql.entities;

public enum DatabaseTable {
    USER("user"),
    ARTICLE("article"),
    COMMENT("comment"),
    USER_ROLE("user_role");

    private final String value;

    public String getValue() {
        return value;
    }

    DatabaseTable(String s) {
        value = s;
    }

    public static DatabaseTable getUser() {
        return DatabaseTable.USER;
    }

    public static DatabaseTable getArticle() {
        return DatabaseTable.ARTICLE;
    }

    public static DatabaseTable getComment() {
        return DatabaseTable.COMMENT;
    }

    public static DatabaseTable getUserRole() {
        return DatabaseTable.USER_ROLE;
    }
}
