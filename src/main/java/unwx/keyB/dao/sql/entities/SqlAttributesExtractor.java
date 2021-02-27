package unwx.keyB.dao.sql.entities;

public interface SqlAttributesExtractor {
    SqlQueryAttributes getFields();
    SqlField getPrimaryKey();
    SqlField getSecondUniqueKey();
}
