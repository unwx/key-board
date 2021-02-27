package unwx.keyB.dao.sql.entities;

import java.util.List;

public class SqlQueryAttributes {

   private final List<SqlField> fields;
   private final SqlField primaryKey;

    public SqlQueryAttributes(List<SqlField> fields, SqlField primaryKey) {
        this.fields = fields;
        this.primaryKey = primaryKey;
    }

    public List<SqlField> getFields() {
        return fields;
    }

    public SqlField getPrimaryKey() {
        return primaryKey;
    }
}
