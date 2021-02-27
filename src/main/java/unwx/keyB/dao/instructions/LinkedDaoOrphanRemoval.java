package unwx.keyB.dao.instructions;

import org.jetbrains.annotations.NotNull;

public interface LinkedDaoOrphanRemoval<Entity, Key> extends LinkedDAO<Entity, Key> {
    void deleteWithOrphan(@NotNull Object id);
}
