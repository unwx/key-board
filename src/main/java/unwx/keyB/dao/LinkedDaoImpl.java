package unwx.keyB.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import unwx.keyB.config.hibernate.HibernateUtil;
import unwx.keyB.dao.instructions.LinkedDaoOrphanRemoval;
import unwx.keyB.dao.sql.SqlGenerator;
import unwx.keyB.dao.sql.entities.DatabaseTable;
import unwx.keyB.dao.sql.entities.SqlAttributesExtractor;
import unwx.keyB.dao.sql.entities.SqlField;
import unwx.keyB.dao.sql.entities.SqlTableRequest;
import unwx.keyB.dao.utils.ComplexDaoUtils;
import unwx.keyB.dao.utils.LinkedDaoUtils;
import unwx.keyB.exceptions.internal.SqlCastException;
import unwx.keyB.exceptions.rest.exceptions.ResourceNotFoundException;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class LinkedDaoImpl<
        Entity extends SqlAttributesExtractor,
        Key extends Serializable,
        DaoUtils extends ComplexDaoUtils<Entity, Key>
        >
        implements LinkedDaoOrphanRemoval<Entity, Key
        > {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private final SqlGenerator sqlGenerator = new SqlGenerator();

    private final DaoUtils daoUtils;
    private final LinkedDaoUtils<Entity, Key> linkedDaoUtils;
    private final DatabaseTable table;

    private final Supplier<Entity> initializedEntitySupplier;

    public LinkedDaoImpl(Supplier<DaoUtils> daoUtilsSupplier,
                         Supplier<DatabaseTable> table,
                         Supplier<Entity> initializedEntitySupplier) {

        this.daoUtils = daoUtilsSupplier.get();
        this.linkedDaoUtils = new LinkedDaoUtils<>(daoUtils);
        this.table = table.get();
        this.initializedEntitySupplier = initializedEntitySupplier;
    }

    @Override
    public Key create(@NotNull final Entity entity,
                      @NotNull final List<SqlField> externalEntities) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            String sql = sqlGenerator.generateCreate(entity, table, externalEntities);
            Object id = session.createSQLQuery(sql).getSingleResult();
            if (id instanceof Serializable) {
                daoUtils.nestedAttributesSqlCreateProcess(session, entity);
                transaction.commit();
                return (Key) id;
            } else throw new SqlCastException("cast exception. Object -> Key");
        }
    }

    @Override
    public Entity readLazy(@NotNull final List<String> columns,
                           @NotNull final SqlField where) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            String sql = sqlGenerator.generateRead(columns, where, table);
            Entity entity;
            if (columns.size() != 1) {
                Object[] result = (Object[]) session.createSQLQuery(sql).getSingleResult();
                entity = daoUtils.extractEntityFromObject(result, columns);
            } else {
                Object result = session.createSQLQuery(sql).getSingleResult();
                entity = daoUtils.extractEntityFromObject(result, columns.get(0));
            }

            transaction.commit();
            return entity;
        } catch (NullPointerException ex) {
            throw new ResourceNotFoundException(where.getColumn() + " = " + where.getValue() + "not found.");
        }
    }

    @Override
    public List<Entity> readManyLazy(@NotNull final List<String> columns,
                                     @NotNull final String where,
                                     final short limit) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            String sql = sqlGenerator.generateReadMany(columns, where, limit, table);
            List<Entity> entities = new LinkedList<>();
            if (columns.size() != 1) {
                List<Object[]> result = session.createSQLQuery(sql).list();
                result.forEach((o) -> entities.add(daoUtils.extractEntityFromObject(o, columns)));
            } else {
                List<Object> result = session.createSQLQuery(sql).list();
                result.forEach((o) -> daoUtils.extractEntityFromObject(o, columns.get(0)));
            }

            transaction.commit();
            return entities;
        } catch (NullPointerException ex) {
            throw new ResourceNotFoundException(where + " not found.");
        }
    }

    @Override
    public Entity readEager(@NotNull final Object linkedId,
                            @NotNull final List<String> columns,
                            @NotNull final List<SqlTableRequest> requests,
                            @NotNull final SqlField where) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Entity entity = readLazy(columns, where);
            linkedDaoUtils.setLinkedEntities(linkedId, requests, entity, session);

            transaction.commit();
            return entity;
        } catch (NullPointerException ex) {
            throw new ResourceNotFoundException(where.getColumn() + " = " + where.getValue() + "not found.");
        }
    }

    @Override
    public List<Entity> readManyEager(@NotNull final Object linkedId,
                                      @NotNull final List<String> columns,
                                      @NotNull final List<SqlTableRequest> requests,
                                      @NotNull final String where,
                                      final short limit) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            List<Entity> entities = readManyLazy(columns, where, limit);
            entities.forEach((a) -> linkedDaoUtils.setLinkedEntities(linkedId, requests, a, session));

            transaction.commit();
            return entities;
        } catch (NullPointerException ex) {
            throw new ResourceNotFoundException(where + " not found.");
        }
    }

    @Override
    public Entity readLinkedEntity(@NotNull final Object linkedId,
                                   @NotNull final SqlTableRequest request) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Entity source = initializedEntitySupplier.get();
            linkedDaoUtils.setLinkedEntity(linkedId, request, source, session);

            transaction.commit();
            return source;
        } catch (NullPointerException ex) {
            throw new ResourceNotFoundException("not found.");
        }
    }

    @Override
    public Entity readLinkedEntities(@NotNull Object linkedId, @NotNull List<SqlTableRequest> request) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Entity source = initializedEntitySupplier.get();
            linkedDaoUtils.setLinkedEntities(linkedId, request, source, session);

            transaction.commit();
            return source;
        } catch (NullPointerException ex) {
            throw new ResourceNotFoundException("not found.");
        }
    }

    @Override
    public void update(@NotNull Entity entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            String sql = sqlGenerator.generateUpdate(entity, table);
            session.createSQLQuery(sql).executeUpdate();
            daoUtils.nestedAttributesSqlUpdateProcess(session, entity);

            transaction.commit();
        }
    }

    @Override
    public void delete(@NotNull SqlField where) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            String sql = sqlGenerator.generateDelete(where, table);
            session.createSQLQuery(sql).executeUpdate();

            transaction.commit();
        }
    }


    @Override
    public void deleteWithOrphan(@NotNull Object id) {
        try (Session session = sessionFactory.openSession()) {
            SqlField userField = new SqlField(id, "id");

            Transaction transaction = session.beginTransaction();

            String sql = sqlGenerator.generateDelete(userField, table);
            session.createSQLQuery(sql).executeUpdate();
            daoUtils.deleteOrphanProcess(session, id);

            transaction.commit();
        }
    }
}
