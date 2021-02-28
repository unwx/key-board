package unwx.keyB.dao;

import org.jetbrains.annotations.NotNull;
import unwx.keyB.dao.entities.DeleteType;
import unwx.keyB.dao.entities.SaveType;
import unwx.keyB.dao.instructions.DefaultDAO;
import unwx.keyB.dao.sql.entities.DatabaseTable;
import unwx.keyB.dao.sql.entities.SqlField;
import unwx.keyB.dao.sql.entities.SqlTableRequest;
import unwx.keyB.dao.utils.impl.CommentComplexDaoUtilsImpl;
import unwx.keyB.domains.Comment;
import unwx.keyB.exceptions.internal.sql.SqlIllegalArgumentException;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class CommentDao implements DefaultDAO<Comment, Long> {

    private final LinkedDaoImpl<
            Comment,
            Long,
            CommentComplexDaoUtilsImpl
            > dao = new LinkedDaoImpl<>
            (
                    CommentComplexDaoUtilsImpl::new,
                    DatabaseTable::getComment,
                    Comment::new);

    @Override
    public Long save(@NotNull Comment e,
                     @NotNull SaveType s) throws SqlIllegalArgumentException {
        if (s == SaveType.CREATE) {
            return dao.create(e, new ArrayList<>() {
                @Serial
                private static final long serialVersionUID = -8378278176449186609L;

                {
                    add(new SqlField(e.getAuthor(), "user_id"));
                    add(new SqlField(e.getArticle(), "article_id"));
                }
            });
        }
        if (s == SaveType.UPDATE) {
            if (e.getId() != null) {
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
    public Comment readLinkedEntities(@NotNull Object linkedId,
                                      @NotNull List<SqlTableRequest> request) {
        return dao.readLinkedEntities(linkedId, request);
    }

    @Override
    public Comment readLinkedEntity(@NotNull Object linkedId,
                                    @NotNull SqlTableRequest request) {
        return dao.readLinkedEntity(linkedId, request);
    }

    @Override
    public Comment readLazy(@NotNull List<String> columns, @NotNull SqlField where) {
        return dao.readLazy(columns, where);
    }

    @Override
    public List<Comment> readManyLazy(@NotNull List<String> columns,
                                      @NotNull String where,
                                      short limit) {
        return dao.readManyLazy(columns, where, limit);
    }

    @Override
    public Comment readEager(@NotNull Object linkedId,
                             @NotNull List<String> columns,
                             @NotNull List<SqlTableRequest> requests,
                             @NotNull SqlField where) {
        return dao.readEager(linkedId, columns, requests, where);
    }

    @Override
    public List<Comment> readManyEager(@NotNull Object linkedId,
                                       @NotNull List<String> columns,
                                       @NotNull List<SqlTableRequest> requests,
                                       @NotNull String where,
                                       short limit) {
        return dao.readManyEager(linkedId, columns, requests, where, limit);
    }
}
