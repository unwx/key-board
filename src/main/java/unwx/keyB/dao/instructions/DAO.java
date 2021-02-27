package unwx.keyB.dao.instructions;

import org.jetbrains.annotations.NotNull;
import unwx.keyB.dao.sql.entities.SqlField;

import java.util.List;

public interface DAO<Entity, Key> {

    Key create(@NotNull final Entity entity, List<SqlField> externalEntities);

    Entity readLazy(@NotNull List<String> columns, @NotNull final SqlField where);

    List<Entity> readManyLazy(@NotNull List<String> columns, @NotNull final String where, final short limit);

    void update(@NotNull final Entity entity);

    void delete(@NotNull final SqlField where);

}
