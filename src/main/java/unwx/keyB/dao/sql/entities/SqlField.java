package unwx.keyB.dao.sql.entities;

public class SqlField {

    private final Object value;
    private final String column;

    public SqlField(Object value, String column) {
        this.value = value;
        this.column = column;
    }

    public Object getValue() {
        return value;
    }

    public String getColumn() {
        return column;
    }
}
