package unwx.keyB.dao.instructions;

import org.jetbrains.annotations.NotNull;
import unwx.keyB.dao.entities.DeleteType;
import unwx.keyB.dao.entities.SaveType;
import unwx.keyB.dao.sql.entities.SqlField;
import unwx.keyB.dao.sql.entities.SqlTableRequest;
import unwx.keyB.exceptions.internal.sql.SqlIllegalArgumentException;

import java.util.List;

public interface DefaultDAO<Entity, Key> {

    Key save(@NotNull final Entity e, @NotNull final SaveType s) throws SqlIllegalArgumentException;

    void delete(@NotNull final SqlField w, @NotNull final DeleteType t) throws SqlIllegalArgumentException;

    Entity readLinkedEntities(@NotNull final Object linkedId, @NotNull final List<SqlTableRequest> request);

    Entity readLinkedEntity(@NotNull final Object linkedId, @NotNull final SqlTableRequest request);

    Entity readLazy(@NotNull final List<String> columns, @NotNull final SqlField where);

    List<Entity> readManyLazy(@NotNull final List<String> columns, @NotNull final String where, final short limit);

    Entity readEager(@NotNull final Object linkedId, @NotNull final List<String> columns, @NotNull final List<SqlTableRequest> nestedEntities, @NotNull final SqlField where);

    List<Entity> readManyEager(@NotNull final Object linkedId, @NotNull final List<String> columns, @NotNull final List<SqlTableRequest> nestedEntities, @NotNull final String where, final short limit);

}
