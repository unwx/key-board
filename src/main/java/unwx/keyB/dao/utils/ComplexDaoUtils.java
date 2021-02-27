package unwx.keyB.dao.utils;

import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import unwx.keyB.dao.sql.entities.DatabaseTable;

import java.io.Serializable;
import java.util.List;

public interface ComplexDaoUtils<Entity, Key extends Serializable> {

    void deleteOrphanProcess(Session session, Object id);

    void nestedAttributesSqlCreateProcess(Session session, Entity e);

    void nestedAttributesSqlUpdateProcess(Session session, Entity e);

    Entity extractEntityFromObject(@NotNull Object[] obj, List<String> columns);

    Entity extractEntityFromObject(@NotNull Object obj, String column);

    void setEntityValueFromObject(Object obj, Entity target, String column);

    void setLinkedValueFromObject(Object obj, Entity target, String column, DatabaseTable table);

    void setLinkedValueFromObject(Object[] obj, Entity target, List<String> columns, DatabaseTable table);

    void setLinkedValuesFromObjects(List<Object> objects, Entity target, String column, DatabaseTable table);

    void setLinkedValuesFromObjects(List<Object[]> objects, Entity target, List<String> columns, DatabaseTable table);

}
