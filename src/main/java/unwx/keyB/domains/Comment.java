package unwx.keyB.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import unwx.keyB.dao.sql.entities.SqlAttributesExtractor;
import unwx.keyB.dao.sql.entities.SqlField;
import unwx.keyB.dao.sql.entities.SqlQueryAttributes;

import javax.persistence.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@SuppressWarnings("unused")
public class Comment implements SqlAttributesExtractor {

    @Id
    private Long id;

    @Column(name = "text", length = 1024)
    private String text;

    @Column(name = "likes", nullable = false)
    private Integer likes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Article article;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User author;

    public Comment(String text,
                   Integer likes,
                   Article article,
                   User author) {
        this.text = text;
        this.likes = likes;
        this.article = article;
        this.author = author;
    }

    public Comment() {
    }

    public Comment(Long id,
                   String text,
                   Integer likes,
                   Article article,
                   User author) {
        this.id = id;
        this.text = text;
        this.likes = likes;
        this.article = article;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public SqlQueryAttributes getFields() {
        List<SqlField> fields = new ArrayList<>() {
            @Serial
            private static final long serialVersionUID = 2008765166480397823L;

            {
                add(new SqlField(id, "id"));
                add(new SqlField(text, "text"));
                add(new SqlField(likes, "likes"));
            }
        };
        return new SqlQueryAttributes(fields, new SqlField(id, "id"));
    }

    @Override
    public SqlField getPrimaryKey() {
        return new SqlField(id, "id");
    }

    @Override
    @Deprecated
    public SqlField getSecondUniqueKey() {
        return null;
    }

    public static class Builder {

        private final Comment comment;

        public Builder() {
            this.comment = new Comment();
        }

        public Builder id(Long id) {
            comment.id = id;
            return this;
        }

        public Builder text(String text) {
            comment.text = text;
            return this;
        }

        public Builder likes(Integer likes) {
            comment.likes = likes;
            return this;
        }

        public Builder article(Article article) {
            comment.article = article;
            return this;
        }

        public Builder author(User author) {
            comment.author = author;
            return this;
        }

        public Comment build() {
            return this.comment;
        }
    }

    public static class Columns {

        private final List<String> columns;

        public Columns() {
            columns = new LinkedList<>();
        }

        public Columns id() {
            columns.add("id");
            return this;
        }

        public Columns text() {
            columns.add("text");
            return this;
        }

        public Columns likes() {
            columns.add("likes");
            return this;
        }

        public List<String> get() {
            return columns;
        }
    }
}
