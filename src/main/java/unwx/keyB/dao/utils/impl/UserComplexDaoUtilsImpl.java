package unwx.keyB.dao.utils.impl;

import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import unwx.keyB.dao.sql.entities.DatabaseTable;
import unwx.keyB.dao.sql.entities.SqlField;
import unwx.keyB.dao.sql.SqlGenerator;
import unwx.keyB.dao.utils.ComplexDaoUtils;
import unwx.keyB.dao.utils.DaoUtils;
import unwx.keyB.domains.User;

import java.io.Serial;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class UserComplexDaoUtilsImpl extends DaoUtils implements ComplexDaoUtils<User, Long> {

    private final SqlGenerator sqlGenerator = new SqlGenerator();

    @Override
    public void nestedAttributesSqlCreateProcess(Session session, User user) {
        List<String> userCommentsSql = new ArrayList<>() {
            @Serial
            private static final long serialVersionUID = 139958953590055958L;

            {
                if (user.getComments() != null) {
                    user.getComments().forEach((e) ->
                            add(sqlGenerator.generateCreate
                                    (
                                            e,
                                            DatabaseTable.COMMENT,
                                            new ArrayList<>() {

                                                @Serial
                                                private static final long serialVersionUID = 4297600759442990590L;

                                                {
                                                    add(new SqlField(e.getId(), "user_id"));
                                                }
                                            })));
                }
            }
        };
        List<String> userArticlesSql = new ArrayList<>() {

            @Serial
            private static final long serialVersionUID = -8441231330277394629L;

            {
                if (user.getArticles() != null) {
                    user.getArticles().forEach((e) ->
                            add(sqlGenerator.generateCreate(
                                    e,
                                    DatabaseTable.ARTICLE,
                                    new ArrayList<>() {


                                        @Serial
                                        private static final long serialVersionUID = 6827205787521944283L;

                                        {
                                            add(new SqlField(e.getId(), "user_id"));
                                        }
                                    })));
                }
            }
        };
        pushQueries(session, userCommentsSql);
        pushQueries(session, userArticlesSql);
    }

    @Override
    public void nestedAttributesSqlUpdateProcess(Session session, User user) {
        List<String> userCommentsSql = new ArrayList<>() {


            @Serial
            private static final long serialVersionUID = -4352158804412687741L;

            {
                if (user.getComments() != null) {
                    user.getComments().forEach((e) ->
                            add(sqlGenerator.generateUpdate
                                    (
                                            e,
                                            DatabaseTable.COMMENT,
                                            new ArrayList<>() {


                                                @Serial
                                                private static final long serialVersionUID = 6039613424565089326L;

                                                {
                                                    add(new SqlField(e.getId(), "user_id"));
                                                }
                                            })));
                }
            }
        };
        List<String> userArticlesSql = new ArrayList<>() {


            @Serial
            private static final long serialVersionUID = 396849040282790729L;

            {
                if (user.getArticles() != null) {
                    user.getArticles().forEach((e) ->
                            add(sqlGenerator.generateUpdate(
                                    e,
                                    DatabaseTable.ARTICLE,
                                    new ArrayList<>() {

                                        @Serial
                                        private static final long serialVersionUID = -9167199912143027082L;

                                        {
                                            add(new SqlField(e.getId(), "user_id"));
                                        }
                                    })));
                }
            }
        };
        pushQueries(session, userCommentsSql);
        pushQueries(session, userArticlesSql);
    }

    @Override
    public void deleteOrphanProcess(Session session, Object id) {
        SqlField orphans = new SqlField(id, "user_id");

        String sqlOrphanComments = sqlGenerator.generateDelete(orphans, DatabaseTable.COMMENT);
        String sqlOrphanArticle = sqlGenerator.generateDelete(orphans, DatabaseTable.ARTICLE);

        session.createSQLQuery(sqlOrphanComments).executeUpdate();
        session.createSQLQuery(sqlOrphanArticle).executeUpdate();
    }

    @Override
    public User extractEntityFromObject(@NotNull Object[] obj, List<String> columns) {
        User user = new User();
        for (int i = 0; i < columns.size(); i++) {
            setEntityValueFromObject(obj[i], user, columns.get(i));
        }
        return user;
    }

    @Override
    public User extractEntityFromObject(@NotNull Object obj, String column) {
        User user = new User();
        setEntityValueFromObject(obj, user, column);
        return user;
    }

    @Override
    public void setEntityValueFromObject(Object obj, User target, String column) {
        switch (column) {
            case "id" -> target.setId(convertBigintToLong((BigInteger) obj));
            case "username" -> target.setUsername((String) obj);
            case "password" -> target.setPassword((String) obj);
            case "active" -> target.setActive((Boolean) obj);
            case "email" -> target.setEmail((String) obj);
            case "avatar_name" -> target.setAvatarName((String) obj);
            case "access_expiration" -> target.setAccessTokenExpiration((String) obj);
            case "refresh_expiration" -> target.setRefreshTokenExpiration((String) obj);
        }
    }

    @Override
    public void setLinkedValueFromObject(Object obj, User target, String column, DatabaseTable table) {
        CommentComplexDaoUtilsImpl commentComplexDaoUtilsImpl = new CommentComplexDaoUtilsImpl();
        ArticleComplexDaoUtilsImpl articleComplexDaoUtilsImpl = new ArticleComplexDaoUtilsImpl();
        switch (table) {
            case ARTICLE -> target.setArticles(
                    Collections.singletonList(
                            articleComplexDaoUtilsImpl.extractEntityFromObject(obj, column)));

            case COMMENT -> target.setComments(
                    Collections.singletonList(
                            commentComplexDaoUtilsImpl.extractEntityFromObject(obj, column)));

            case USER_ROLE -> target.setRoles(
                    Collections.singletonList(UserRoleExtractor.extractRolesFromObject(obj).get(0)));
        }
    }

    @Override
    public void setLinkedValueFromObject(Object[] obj, User target, List<String> columns, DatabaseTable table) {
        CommentComplexDaoUtilsImpl commentComplexDaoUtilsImpl = new CommentComplexDaoUtilsImpl();
        ArticleComplexDaoUtilsImpl articleComplexDaoUtilsImpl = new ArticleComplexDaoUtilsImpl();
        switch (table) {
            case ARTICLE -> target.setArticles(
                    Collections.singletonList(
                            articleComplexDaoUtilsImpl.extractEntityFromObject(obj, columns)));

            case COMMENT -> target.setComments(
                    Collections.singletonList(
                            commentComplexDaoUtilsImpl.extractEntityFromObject(obj, columns)));

            case USER_ROLE -> target.setRoles(
                    Collections.singletonList(UserRoleExtractor.extractRolesFromObject(obj).get(0)));
        }
    }

    @Override
    public void setLinkedValuesFromObjects(List<Object> objects, User target, String column, DatabaseTable table) {
        ArticleComplexDaoUtilsImpl articleComplexDaoUtilsImpl = new ArticleComplexDaoUtilsImpl();
        CommentComplexDaoUtilsImpl commentComplexDaoUtilsImpl = new CommentComplexDaoUtilsImpl();
        switch (table) {
            case ARTICLE -> target.setArticles(new LinkedList<>() {
                @Serial
                private static final long serialVersionUID = 8295985481023044957L;

                {
                    objects.forEach((o) -> add(articleComplexDaoUtilsImpl.extractEntityFromObject(o, column)));
                }
            });

            case COMMENT -> target.setComments(new LinkedList<>() {
                @Serial
                private static final long serialVersionUID = 5019728351925851744L;

                {
                    objects.forEach((o) -> add(commentComplexDaoUtilsImpl.extractEntityFromObject(o, column)));
                }
            });

            case USER_ROLE -> target.setRoles(UserRoleExtractor.extractRolesFromObject(objects.get(0)));
        }
    }

    @Override
    public void setLinkedValuesFromObjects(List<Object[]> objects, User target, List<String> columns, DatabaseTable table) {
        ArticleComplexDaoUtilsImpl articleComplexDaoUtilsImpl = new ArticleComplexDaoUtilsImpl();
        CommentComplexDaoUtilsImpl commentComplexDaoUtilsImpl = new CommentComplexDaoUtilsImpl();
        switch (table) {
            case ARTICLE -> target.setArticles(new LinkedList<>() {


                @Serial
                private static final long serialVersionUID = -1620679201894977759L;

                {
                    objects.forEach((o) -> add(articleComplexDaoUtilsImpl.extractEntityFromObject(o, columns)));
                }
            });

            case COMMENT -> target.setComments(new LinkedList<>() {

                @Serial
                private static final long serialVersionUID = 5798706358380910006L;

                {
                    objects.forEach((o) -> add(commentComplexDaoUtilsImpl.extractEntityFromObject(o, columns)));
                }
            });

            case USER_ROLE -> target.setRoles(UserRoleExtractor.extractRolesFromObject(objects.get(0)));
        }
    }
}
