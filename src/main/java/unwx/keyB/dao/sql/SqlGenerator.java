package unwx.keyB.dao.sql;

import unwx.keyB.dao.sql.entities.DatabaseTable;
import unwx.keyB.dao.sql.entities.SqlAttributesExtractor;
import unwx.keyB.dao.sql.entities.SqlField;
import unwx.keyB.dao.sql.entities.SqlQueryAttributes;
import unwx.keyB.exceptions.internal.dao.SqlNullPrimaryKeyException;

import java.util.LinkedList;
import java.util.List;

/**
 * I had problems with hibernate,
 * it didn't insert automatic "id" values through setters,
 * so I will do it myself (auto_increment),
 * and I will generate sql queries
 * without reflection, cool!
 * <p>
 * creating hql request like update / create... etc.
 */
public class SqlGenerator {

    public SqlGenerator() {
    }

    public String generateCreate(final SqlAttributesExtractor extractor,
                                 final DatabaseTable table) {
        SqlQueryAttributes attributes = extractor.getFields();
        List<String> columns = new LinkedList<>();
        List<Object> values = new LinkedList<>();

        nullFilter(attributes, columns, values);

        return constructCreate(table, columns, values);
    }

    public String generateCreate(final SqlAttributesExtractor extractor,
                                 final DatabaseTable table,
                                 final List<SqlField> additional) {

        SqlQueryAttributes attributes = extractor.getFields();
        List<String> columns = new LinkedList<>();
        List<Object> values = new LinkedList<>();

        nullFilter(attributes, columns, values);
        nullFilter(additional, columns, values);
        return constructCreate(table, columns, values);
    }


    public String generateRead(final List<String> columns,
                               final SqlField where,
                               final DatabaseTable table) {
        StringBuilder sb = new StringBuilder("SELECT ");
        columns.forEach(
                (e) -> sb
                        .append("`")
                        .append(e)
                        .append("`,"));
        sb.deleteCharAt(sb.length() - 1);
        sb
                .append(" FROM ")
                .append("`")
                .append(table.getValue())
                .append("`")
                .append(" WHERE ")
                .append("`")
                .append(where.getColumn())
                .append("`")
                .append("=")
                .append("'")
                .append(where.getValue())
                .append("'")
                .append(";");
        return sb.toString();
    }

    /**
     * @param where ... WHERE ${where} LIMIT ${limit};
     */
    public String generateReadMany(final List<String> columns,
                                   final String where,
                                   final short limit,
                                   final DatabaseTable table) {
        StringBuilder sb = new StringBuilder("SELECT ");
        columns.forEach(
                (e) -> sb
                        .append("`")
                        .append(e)
                        .append("`,"));
        sb.deleteCharAt(sb.length() - 1);
        sb
                .append(" FROM ")
                .append("`")
                .append(table.getValue())
                .append("`")
                .append(" WHERE ")
                .append(where)
                .append(" LIMIT ")
                .append(limit)
                .append(";");
        return sb.toString();
    }

    public String generateUpdate(final SqlAttributesExtractor extractor,
                                 final DatabaseTable table) {
        SqlField pk = getTargetKey(extractor);

        SqlQueryAttributes attributes = extractor.getFields();
        List<String> columns = new LinkedList<>();
        List<Object> values = new LinkedList<>();

        nullFilter(attributes, columns, values);

        return constructUpdate(table, columns, values, pk);
    }

    public String generateUpdate(final SqlAttributesExtractor extractor,
                                 final DatabaseTable table,
                                 final List<SqlField> additional) {
        SqlField pk = getTargetKey(extractor);

        SqlQueryAttributes attributes = extractor.getFields();
        List<String> columns = new LinkedList<>();
        List<Object> values = new LinkedList<>();

        nullFilter(attributes, columns, values);
        nullFilter(additional, columns, values);

        return constructUpdate(table, columns, values, pk);
    }

    public String generateDelete(final SqlField where,
                                 final DatabaseTable table) {
        return "DELETE FROM " +
                "`" +
                table.getValue() +
                "`" +
                " WHERE " +
                "`" +
                where.getColumn() +
                "`" +
                "=" +
                "'" +
                where.getValue() +
                "'" +
                ";";
    }

    public String generateGetLastId() {
        return "SELECT LAST_INSERT_ID();";
    }

    private String constructCreate(DatabaseTable table,
                                   List<String> columns,
                                   List<Object> values) {

        StringBuilder sb = new StringBuilder();
        if (!columns.isEmpty()) {
            sb
                    .append("INSERT INTO ")
                    .append("`")
                    .append(table.getValue())
                    .append("`")
                    .append(" (")
                    .append(columnsToSingleString(columns))
                    .append(") ")
                    .append("VALUES (")
                    .append(valuesToSingleString(values))
                    .append(");");
        }
        return sb.toString();
    }

    private String constructUpdate(DatabaseTable table,
                                   List<String> columns,
                                   List<Object> values,
                                   SqlField where) {
        StringBuilder sb = new StringBuilder();
        if (!columns.isEmpty()) {
            sb
                    .append("UPDATE ")
                    .append(table.getValue())
                    .append(" SET ")
                    .append(columnsEqualsValues(columns, values))
                    .append(" WHERE ")
                    .append("`")
                    .append(where.getColumn())
                    .append("`")
                    .append("=")
                    .append("'")
                    .append(where.getValue())
                    .append("'")
                    .append(";");
        }
        return sb.toString();
    }

    /**
     * @param columns NOT NULL. NOT EMPTY
     */
    private String columnsToSingleString(List<String> columns) {
        StringBuilder sb = new StringBuilder();
        columns.forEach((e) -> sb
                .append("`")
                .append(e)
                .append("`")
                .append(","));
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * @param values NOT NULL. NOT EMPTY
     */
    private String valuesToSingleString(List<Object> values) {
        StringBuilder sb = new StringBuilder();
        values.forEach((e) -> sb
                .append("'")
                .append(e)
                .append("'")
                .append(","));
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * @param values  NOT NULL. NOT EMPTY
     * @param columns NOT NULL. NOT EMPTY
     */
    private String columnsEqualsValues(List<String> columns,
                                       List<Object> values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            sb
                    .append("`")
                    .append(columns.get(i))
                    .append("`")
                    .append("=")
                    .append("'")
                    .append(values.get(i))
                    .append("'")
                    .append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private void nullFilter(SqlQueryAttributes attributes,
                            List<String> columns,
                            List<Object> values) {
        for (SqlField field : attributes.getFields()) {
            if (field.getValue() != null) {
                columns.add(field.getColumn());
                values.add(field.getValue());
            }
        }
    }

    private void nullFilter(List<SqlField> fields,
                            List<String> columns,
                            List<Object> values) {
        for (SqlField field : fields) {
            if (field.getValue() != null) {
                columns.add(field.getColumn());
                values.add(field.getValue());
            }
        }
    }

    private SqlField getTargetKey(SqlAttributesExtractor extractor) {
        SqlField pk;
        if (extractor.getPrimaryKey() != null && extractor.getPrimaryKey().getValue() != null) {
            pk = extractor.getPrimaryKey();
        } else if (extractor.getSecondUniqueKey() != null && extractor.getSecondUniqueKey().getValue() != null)
            pk = extractor.getSecondUniqueKey();
        else throw new SqlNullPrimaryKeyException("primary key is null.");
        return pk;
    }
}
