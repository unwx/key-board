package unwx.keyB.dao.utils.impl;

import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import unwx.keyB.dao.sql.SqlGenerator;
import unwx.keyB.dao.sql.entities.DatabaseTable;
import unwx.keyB.dao.sql.entities.SqlField;
import unwx.keyB.dao.utils.ComplexDaoUtils;
import unwx.keyB.dao.utils.DaoUtils;
import unwx.keyB.domains.Article;
import unwx.keyB.domains.User;

import java.io.Serial;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ArticleComplexDaoUtilsImpl extends DaoUtils implements ComplexDaoUtils<Article, Long> {

    private final SqlGenerator sqlGenerator = new SqlGenerator();

    @Override
    public void deleteOrphanProcess(Session session, Object id) {
        SqlField orphans = new SqlField(id, "article_id");
        String sqlOrphanComments = sqlGenerator.generateDelete(orphans, DatabaseTable.COMMENT);
        session.createSQLQuery(sqlOrphanComments).executeUpdate();
    }

    @Override
    public void nestedAttributesSqlCreateProcess(Session session, Article article) {
        List<String> articleCommentsSql = new ArrayList<>() {

            @Serial
            private static final long serialVersionUID = -1435668693122651010L;

            {
                if (article.getComments() != null) {
                    article.getComments().forEach((e) ->
                            add(sqlGenerator.generateCreate(
                                    e,
                                    DatabaseTable.COMMENT,
                                    new ArrayList<>() {


                                        @Serial
                                        private static final long serialVersionUID = 6905326067805061419L;

                                        {
                                            add(new SqlField(e.getId(), "article_id"));
                                        }
                                    })));
                }
            }
        };
        pushQueries(session, articleCommentsSql);
    }

    @Override
    public void nestedAttributesSqlUpdateProcess(Session session, Article article) {
        List<String> articleCommentsSql = new ArrayList<>() {


            @Serial
            private static final long serialVersionUID = 717141510932915986L;

            {
                if (article.getComments() != null) {
                    article.getComments().forEach((e) ->
                            add(sqlGenerator.generateUpdate(
                                    e,
                                    DatabaseTable.COMMENT,
                                    new ArrayList<>() {


                                        @Serial
                                        private static final long serialVersionUID = -7675365327296636848L;

                                        {
                                            add(new SqlField(e.getId(), "article_id"));
                                        }
                                    })));
                }
            }
        };
        pushQueries(session, articleCommentsSql);
    }

    @Override
    public Article extractEntityFromObject(@NotNull Object[] obj, List<String> columns) {
        Article article = new Article();
        for (int i = 0; i < columns.size(); i++) {
            setEntityValueFromObject(obj[i], article, columns.get(i));
        }
        return article;
    }

    @Override
    public Article extractEntityFromObject(@NotNull Object obj, String column) {
        Article article = new Article();
        setEntityValueFromObject(obj, article, column);
        return article;
    }

    @Override
    public void setEntityValueFromObject(Object obj, Article target, String column) {
        switch (column) {
            case "id" -> target.setId(convertBigintToLong((BigInteger) obj));
            case "title" -> target.setTitle((String) obj);
            case "link" -> target.setLink((String) obj);
            case "text" -> target.setText((String) obj);
            case "creation_date" -> target.setCreationDate((convertTimestampToLocalDateTime((Timestamp) obj)));
            case "likes" -> target.setLikes((Integer) obj);
            case "user_id" -> target.setAuthor(new User.Builder().id(convertBigintToLong((BigInteger) obj)).build());
        }
    }

    @Override
    public void setLinkedValueFromObject(Object obj, Article target, String column, DatabaseTable table) {
        CommentComplexDaoUtilsImpl commentComplexDaoUtilsImpl = new CommentComplexDaoUtilsImpl();
        UserComplexDaoUtilsImpl userComplexDaoUtilsImpl = new UserComplexDaoUtilsImpl();
        switch (table) {
            case USER -> target.setAuthor(userComplexDaoUtilsImpl.extractEntityFromObject(obj, column));

            case COMMENT -> target.setComments(
                    Collections.singletonList(
                            commentComplexDaoUtilsImpl.extractEntityFromObject(obj, column)));
        }
    }

    @Override
    public void setLinkedValueFromObject(Object[] obj, Article target, List<String> columns, DatabaseTable table) {
        CommentComplexDaoUtilsImpl commentComplexDaoUtilsImpl = new CommentComplexDaoUtilsImpl();
        UserComplexDaoUtilsImpl userComplexDaoUtilsImpl = new UserComplexDaoUtilsImpl();
        switch (table) {
            case USER -> target.setAuthor(userComplexDaoUtilsImpl.extractEntityFromObject(obj, columns));

            case COMMENT -> target.setComments(
                    Collections.singletonList(
                            commentComplexDaoUtilsImpl.extractEntityFromObject(obj, columns)));
        }
    }

    @Override
    public void setLinkedValuesFromObjects(List<Object> objects, Article target, String column, DatabaseTable table) {
        CommentComplexDaoUtilsImpl commentComplexDaoUtilsImpl = new CommentComplexDaoUtilsImpl();
        UserComplexDaoUtilsImpl userComplexDaoUtils = new UserComplexDaoUtilsImpl();


        switch (table) {
            case COMMENT -> target.setComments(new LinkedList<>() {

                @Serial
                private static final long serialVersionUID = 3571199152744582831L;

                {
                    objects.forEach((o) -> add(commentComplexDaoUtilsImpl.extractEntityFromObject(o, column)));
                }
            });

            case USER -> target.setAuthor(userComplexDaoUtils.extractEntityFromObject(objects.get(0), column));
        }
    }

    @Override
    public void setLinkedValuesFromObjects(List<Object[]> objects, Article target, List<String> columns, DatabaseTable table) {
        CommentComplexDaoUtilsImpl commentComplexDaoUtilsImpl = new CommentComplexDaoUtilsImpl();
        UserComplexDaoUtilsImpl userComplexDaoUtils = new UserComplexDaoUtilsImpl();

        switch (table) {
            case COMMENT -> target.setComments(new LinkedList<>() {

                @Serial
                private static final long serialVersionUID = 8499084425730378116L;

                {
                    objects.forEach((o) -> add(commentComplexDaoUtilsImpl.extractEntityFromObject(o, columns)));
                }
            });

            case USER -> target.setAuthor(userComplexDaoUtils.extractEntityFromObject(objects.get(0), columns));
        }
    }
}
