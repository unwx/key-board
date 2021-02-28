package unwx.keyB.dao;

import org.jetbrains.annotations.NotNull;
import unwx.keyB.dao.entities.DeleteType;
import unwx.keyB.dao.entities.SaveType;
import unwx.keyB.dao.instructions.DefaultDAO;
import unwx.keyB.dao.sql.entities.DatabaseTable;
import unwx.keyB.dao.sql.entities.SqlField;
import unwx.keyB.dao.sql.entities.SqlTableRequest;
import unwx.keyB.dao.utils.impl.ArticleComplexDaoUtilsImpl;
import unwx.keyB.domains.Article;
import unwx.keyB.exceptions.internal.sql.SqlIllegalArgumentException;

import java.util.Collections;
import java.util.List;

public class ArticleDao implements DefaultDAO<Article, Long> {

    private final LinkedDaoImpl<
            Article,
            Long,
            ArticleComplexDaoUtilsImpl
            > dao = new LinkedDaoImpl<>
            (
                    ArticleComplexDaoUtilsImpl::new,
                    DatabaseTable::getArticle,
                    Article::new);

    @Override
    public Long save(@NotNull Article e,
                     @NotNull SaveType s) throws SqlIllegalArgumentException {
        if (s == SaveType.CREATE) {
            if (e.getAuthor().getId() != null)
                return dao.create(e, Collections.singletonList(new SqlField(e.getAuthor().getId(), "id")));
            throw new SqlIllegalArgumentException("nested arguments required.");
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
    public Article readLinkedEntities(@NotNull Object linkedId,
                                      @NotNull List<SqlTableRequest> request) {
        return dao.readLinkedEntities(linkedId, request);
    }

    @Override
    public Article readLinkedEntity(@NotNull Object linkedId,
                                    @NotNull SqlTableRequest request) {
        return dao.readLinkedEntity(linkedId, request);
    }

    @Override
    public Article readLazy(@NotNull List<String> columns, @NotNull SqlField where) {
        return dao.readLazy(columns, where);
    }

    @Override
    public List<Article> readManyLazy(@NotNull List<String> columns,
                                      @NotNull String where,
                                      short limit) {
        return dao.readManyLazy(columns, where, limit);
    }

    @Override
    public Article readEager(@NotNull Object linkedId,
                             @NotNull List<String> columns,
                             @NotNull List<SqlTableRequest> requests,
                             @NotNull SqlField where) {
        return dao.readEager(linkedId, columns, requests, where);
    }

    @Override
    public List<Article> readManyEager(@NotNull Object linkedId,
                                       @NotNull List<String> columns,
                                       @NotNull List<SqlTableRequest> requests,
                                       @NotNull String where,
                                       short limit) {
        return dao.readManyEager(linkedId, columns, requests, where, limit);
    }

}
