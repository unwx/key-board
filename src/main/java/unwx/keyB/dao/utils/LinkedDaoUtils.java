package unwx.keyB.dao.utils;

import org.hibernate.Session;
import unwx.keyB.dao.sql.SqlGenerator;
import unwx.keyB.dao.sql.entities.SqlTableRequest;
import unwx.keyB.exceptions.internal.dao.SqlIllegalArgumentException;

import java.io.Serializable;
import java.util.List;

public class LinkedDaoUtils<Entity, Key extends Serializable> extends DaoUtils{

    private final SqlGenerator sqlGenerator = new SqlGenerator();
    private final ComplexDaoUtils<Entity, Key> daoUtils;

    public LinkedDaoUtils(ComplexDaoUtils<Entity, Key> daoUtils) {
        this.daoUtils = daoUtils;
    }

    public void setLinkedEntities(Object linkedId,
                                  List<SqlTableRequest> requests,
                                  Entity e,
                                  Session session) {
        for (SqlTableRequest s : requests) {
            _setLinkedEntity(linkedId, s, e, session);
        }
    }

    public void setLinkedEntity(Object linkedId,
                                SqlTableRequest request,
                                Entity e,
                                Session session) {
        _setLinkedEntity(linkedId, request, e, session);
    }

    public void setLinkedEntitiesManyToMany(List<Object> linkedIds,
                                            List<SqlTableRequest> requests,
                                            List<Entity> entities,
                                            Session session) {
        if (linkedIds.size() != entities.size())
            throw new SqlIllegalArgumentException("list<id>.size must be equal list<entity>.size!");

        for (int i = 0; i < linkedIds.size(); i++) {
            _setLinkedEntity(linkedIds.get(i), requests.get(i), entities.get(i), session);
        }
    }

    public void setLinkedEntitiesManyToMany(List<Object> linkedIds,
                                            SqlTableRequest request,
                                            List<Entity> entities,
                                            Session session) {
        if (linkedIds.size() != entities.size())
            throw new SqlIllegalArgumentException("list<id>.size must be equal list<entity>.size!");

        for (int i = 0; i < linkedIds.size(); i++) {
            _setLinkedEntity(linkedIds.get(i), request, entities.get(i), session);
        }
    }

    @SuppressWarnings("unchecked")
    private void _setLinkedEntity(Object linkedId,
                                  SqlTableRequest request,
                                  Entity e,
                                  Session session) {
        List<String> columns = request.getColumns();
        String sql;
        if (request.isNested()) {
            sql = sqlGenerator.generateReadMany(
                    request.getColumns(),
                    "`" + request.getLinkedColumn() + "`" + "=" + "'" + linkedId + "'",
                    request.getLimit(),
                    request.getTable());
        } else {
            sql = sqlGenerator.generateReadMany(
                    request.getColumns(),
                    "`id`=" + "'" + linkedId + "'",
                    request.getLimit(),
                    request.getTable());
        }
        if (request.getLimit() > 1) {
            if (columns.size() > 1) {
                daoUtils.setLinkedValuesFromObjects(
                        session.createSQLQuery(sql).list(),
                        e,
                        columns,
                        request.getTable());
            } else {
                daoUtils.setLinkedValueFromObject(
                        session.createSQLQuery(sql).uniqueResult(),
                        e,
                        columns.get(0),
                        request.getTable());
            }
        } else {
            if (columns.size() > 1) {
                daoUtils.setLinkedValueFromObject(
                        (Object[]) session.createSQLQuery(sql).uniqueResult(),
                        e,
                        columns,
                        request.getTable());
            } else {
                daoUtils.setLinkedValueFromObject(
                        session.createSQLQuery(sql).uniqueResult(),
                        e,
                        columns.get(0),
                        request.getTable());
            }
        }
    }
}
