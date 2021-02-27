package unwx.keyB.dao.sql.entities;

import java.util.List;

public class SqlTableRequest {

    private final DatabaseTable table;
    private final List<String> columns;
    private short limit = 50;
    private final boolean nested;

    public SqlTableRequest(DatabaseTable table, List<String> columns, boolean nested) {
        this.table = table;
        this.columns = columns;
        this.nested = nested;
    }

    public SqlTableRequest(DatabaseTable table, List<String> columns, short limit, boolean nested) {
        this.table = table;
        this.columns = columns;
        this.limit = limit;
        this.nested = nested;
    }

    public DatabaseTable getTable() {
        return table;
    }

    public short getLimit() {
        return limit;
    }

    public void setLimit(short limit) {
        this.limit = limit;
    }

    public List<String> getColumns() {
        return columns;
    }

    public boolean isNested() {
        return nested;
    }
}
