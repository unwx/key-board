package unwx.keyB.dao.utils.impl;

import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import unwx.keyB.dao.sql.SqlGenerator;
import unwx.keyB.dao.sql.entities.DatabaseTable;
import unwx.keyB.dao.utils.ComplexDaoUtils;
import unwx.keyB.dao.utils.DaoUtils;
import unwx.keyB.domains.Article;
import unwx.keyB.domains.Comment;
import unwx.keyB.domains.User;

import java.math.BigInteger;
import java.util.List;

public class CommentComplexDaoUtilsImpl extends DaoUtils implements ComplexDaoUtils<Comment, Long> {

    private final SqlGenerator sqlGenerator = new SqlGenerator();

    @Override
    @Deprecated
    public void deleteOrphanProcess(Session session, Object id) {
        // no orphan yet
    }

    @Override
    @Deprecated
    public void nestedAttributesSqlCreateProcess(Session session, Comment e) {
        // no nested yet
    }

    @Override
    @Deprecated
    public void nestedAttributesSqlUpdateProcess(Session session, Comment e) {
        // no nested yet
    }

    @Override
    public Comment extractEntityFromObject(@NotNull Object[] obj, List<String> columns) {
        Comment comment = new Comment();
        for (int i = 0; i < columns.size(); i++) {
            setEntityValueFromObject(obj[i], comment, columns.get(i));
        }
        return comment;
    }

    @Override
    public Comment extractEntityFromObject(@NotNull Object obj, String column) {
        Comment comment = new Comment();
        setEntityValueFromObject(obj, comment, column);
        return comment;
    }

    @Override
    public void setEntityValueFromObject(Object obj, Comment target, String column) {
        switch (column) {
            case "id" -> target.setId(convertBigintToLong((BigInteger) obj));
            case "text" -> target.setText((String) obj);
            case "likes" -> target.setLikes((Integer) obj);
            case "user_id" -> target.setAuthor(new User.Builder().id(convertBigintToLong((BigInteger) obj)).build());
            case "article_id" -> target.setArticle(new Article.Builder().id(convertBigintToLong((BigInteger) obj)).build());
        }
    }

    @Override
    public void setLinkedValueFromObject(Object obj, Comment target, String column, DatabaseTable table) {
        UserComplexDaoUtilsImpl userComplexDaoUtilsImpl = new UserComplexDaoUtilsImpl();
        ArticleComplexDaoUtilsImpl articleComplexDaoUtilsImpl = new ArticleComplexDaoUtilsImpl();
        switch (table) {
            case USER -> target.setAuthor(userComplexDaoUtilsImpl.extractEntityFromObject(obj, column));

            case ARTICLE -> target.setArticle(articleComplexDaoUtilsImpl.extractEntityFromObject(obj, column));
        }
    }

    @Override
    public void setLinkedValueFromObject(Object[] obj, Comment target, List<String> columns, DatabaseTable table) {
        UserComplexDaoUtilsImpl userComplexDaoUtilsImpl = new UserComplexDaoUtilsImpl();
        ArticleComplexDaoUtilsImpl articleComplexDaoUtilsImpl = new ArticleComplexDaoUtilsImpl();
        switch (table) {
            case USER -> target.setAuthor(userComplexDaoUtilsImpl.extractEntityFromObject(obj, columns));

            case ARTICLE -> target.setArticle(articleComplexDaoUtilsImpl.extractEntityFromObject(obj, columns));
        }
    }

    @Override
    @Deprecated
    public void setLinkedValuesFromObjects(List<Object> objects, Comment target, String column, DatabaseTable table) {
        // no nested
    }

    @Override
    @Deprecated
    public void setLinkedValuesFromObjects(List<Object[]> objects, Comment target, List<String> columns, DatabaseTable table) {
        // no nested
    }
}
