package unwx.keyB.dao.instructions;

import org.jetbrains.annotations.NotNull;
import unwx.keyB.dao.sql.entities.SqlField;
import unwx.keyB.dao.sql.entities.SqlTableRequest;

import java.util.List;

public interface LinkedDAO<Entity, Key> extends DAO<Entity, Key> {
    Entity readEager(@NotNull Object linkedId, @NotNull List<String> columns, @NotNull final List<SqlTableRequest> nestedEntities, @NotNull final SqlField where);

    List<Entity> readManyEager(@NotNull Object linkedId, @NotNull List<String> columns, @NotNull final List<SqlTableRequest> nestedEntities, @NotNull final String where, final short limit);

    Entity readLinkedEntity(@NotNull Object linkedId, @NotNull final SqlTableRequest request);

    Entity readLinkedEntities(@NotNull Object linkedId, @NotNull final List<SqlTableRequest> request);
}
