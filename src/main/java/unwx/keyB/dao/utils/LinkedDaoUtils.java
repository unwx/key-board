package unwx.keyB.dao.utils;

import org.hibernate.Session;
import unwx.keyB.dao.sql.SqlGenerator;
import unwx.keyB.dao.sql.entities.SqlTableRequest;

import java.io.Serializable;
import java.util.List;

public class LinkedDaoUtils<Entity, Key extends Serializable> {

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
                    request.getLinkedColumn() + " = " + linkedId,
                    request.getLimit(),
                    request.getTable());
        } else {
            sql = sqlGenerator.generateReadMany(
                    request.getColumns(),
                    "id = " + linkedId,
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
                        (Object[]) session.createSQLQuery(sql).getSingleResult(),
                        e,
                        columns,
                        request.getTable());
            } else {
                daoUtils.setLinkedValueFromObject(
                        session.createSQLQuery(sql).getSingleResult(),
                        e,
                        columns.get(0),
                        request.getTable());
            }
        }
    }
}
