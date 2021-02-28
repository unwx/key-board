package unwx.keyB.dao.sql.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

public interface SqlAttributesExtractor {
    @JsonIgnore
    SqlQueryAttributes getFields();
    @JsonIgnore
    SqlField getPrimaryKey();
    @JsonIgnore
    SqlField getSecondUniqueKey();
}
