package unwx.keyB.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import unwx.keyB.config.hibernate.HibernateUtil;
import unwx.keyB.dao.entities.DeleteType;
import unwx.keyB.dao.entities.SaveType;
import unwx.keyB.dao.instructions.DefaultDAO;
import unwx.keyB.dao.sql.entities.DatabaseTable;
import unwx.keyB.dao.sql.entities.SqlField;
import unwx.keyB.dao.sql.entities.SqlTableRequest;
import unwx.keyB.dao.utils.impl.UserComplexDaoUtilsImpl;
import unwx.keyB.dao.utils.impl.UserRoleInserter;
import unwx.keyB.domains.User;
import unwx.keyB.exceptions.internal.dao.SqlIllegalArgumentException;

import java.util.Collections;
import java.util.List;

public class UserDao implements DefaultDAO<User, Long> {

    private final LinkedDaoImpl<
            User,
            Long,
            UserComplexDaoUtilsImpl
            > dao = new LinkedDaoImpl<>
            (
                    UserComplexDaoUtilsImpl::new,
                    DatabaseTable::getUser,
                    User::new);

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public Long save(@NotNull User e,
                     @NotNull SaveType s) throws SqlIllegalArgumentException {
        if (s == SaveType.CREATE) {
            Long id = dao.create(e, Collections.emptyList());
            obligatoryFieldsProcess(e, id);
            return id;
        }
        if (s == SaveType.UPDATE) {
            if (e.getId() != null || e.getSecondKey() != null) {
                dao.update(e);
                return e.getId();
            }
        }
        throw new SqlIllegalArgumentException("illegal arguments");
    }

    @Override
    public void delete(@NotNull SqlField w,
                       @NotNull DeleteType t) throws SqlIllegalArgumentException {
        if (t == DeleteType.SINGLE) {
            dao.delete(w);
        }
        if (t == DeleteType.ORPHAN_REMOVAL && w.getColumn().equals("id")) {
            dao.delete(new SqlField(w.getValue(), "id"));
            return;
        }
        throw new SqlIllegalArgumentException("cannot delete orphans without id argument.");
    }

    @Override
    public User readLinkedEntities(@NotNull Object linkedId,
                                   @NotNull List<SqlTableRequest> request) {
        return dao.readLinkedEntities(linkedId, request);
    }

    @Override
    public User readLinkedEntity(@NotNull Object linkedId,
                                 @NotNull SqlTableRequest request) {
        return dao.readLinkedEntity(linkedId, request);
    }

    @Override
    public User readLazy(@NotNull List<String> columns, @NotNull SqlField where) {
        return dao.readLazy(columns, where);
    }

    @Override
    public List<User> readManyLazy(@NotNull List<String> columns,
                                   @NotNull String where,
                                   short limit) {
        return dao.readManyLazy(columns, where, limit);
    }

    @Override
    public User readEager(@NotNull Object linkedId,
                          @NotNull List<String> columns,
                          @NotNull List<SqlTableRequest> requests,
                          @NotNull SqlField where) {
        return dao.readEager(linkedId, columns, requests, where);
    }

    @Override
    public List<User> readManyEager(@NotNull Object linkedId,
                                    @NotNull List<String> columns,
                                    @NotNull List<SqlTableRequest> requests,
                                    @NotNull String where,
                                    short limit) {
        return dao.readManyEager(linkedId, columns, requests, where, limit);
    }

    @Override
    public List<User> readLinkedEntitiesManyToMany(@NotNull List<Object> linkedIds,
                                                   @NotNull List<SqlTableRequest> requests) {
        return dao.readLinkedEntitiesManyToMany(linkedIds, requests);
    }

    @Override
    public List<User> readLinkedEntitiesManyToMany(@NotNull List<Object> linkedIds,
                                                   @NotNull SqlTableRequest requests) {
        return dao.readLinkedEntitiesManyToMany(linkedIds, requests);
    }

    private void obligatoryFieldsProcess(User user, Long id) {
        if (user.getRoles() == null || user.getRoles().isEmpty())
            throw new SqlIllegalArgumentException("no user roles");
        UserRoleInserter userRoleInserter = new UserRoleInserter();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            userRoleInserter.setRoles(user.getRoles(), id, session);

            transaction.commit();
        }
    }
}
