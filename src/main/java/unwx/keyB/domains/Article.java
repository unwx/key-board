package unwx.keyB.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import unwx.keyB.dao.sql.entities.SqlAttributesExtractor;
import unwx.keyB.dao.sql.entities.SqlField;
import unwx.keyB.dao.sql.entities.SqlQueryAttributes;

import javax.persistence.*;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@SuppressWarnings("unused")
public class Article implements SqlAttributesExtractor {

    @Id
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "article", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @Column(name = "likes", nullable = false)
    private Integer likes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User author;

    public Article(String title,
                   String link,
                   String text,
                   LocalDateTime creationDate,
                   List<Comment> comments,
                   Integer likes,
                   User author) {
        this.title = title;
        this.link = link;
        this.text = text;
        this.creationDate = creationDate;
        this.comments = comments;
        this.likes = likes;
        this.author = author;
    }

    public Article() {
    }

    public Article(Long id,
                   String title,
                   String link,
                   String text,
                   LocalDateTime creationDate,
                   List<Comment> comments,
                   Integer likes,
                   User author) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.text = text;
        this.creationDate = creationDate;
        this.comments = comments;
        this.likes = likes;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
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
            private static final long serialVersionUID = -2761899813977990817L;

            {
                add(new SqlField(id, "id"));
                add(new SqlField(title, "title"));
                add(new SqlField(link, "link"));
                add(new SqlField(text, "text"));
                add(new SqlField(creationDate, "creation_date"));
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

        private final Article article;

        public Builder() {
            this.article = new Article();
        }

        public Builder id(Long id) {
            article.id = id;
            return this;
        }

        public Builder title(String title) {
            article.title = title;
            return this;
        }

        public Builder link(String link) {
            article.link = link;
            return this;
        }

        public Builder text(String text) {
            article.text = text;
            return this;
        }

        public Builder creationDate(LocalDateTime creationDate) {
            article.creationDate = creationDate;
            return this;
        }

        public Builder comments(List<Comment> comments) {
            article.comments = comments;
            return this;
        }

        public Builder likes(Integer likes) {
            article.likes = likes;
            return this;
        }

        public Builder author(User author) {
            article.author = author;
            return this;
        }

        public Article build() {
            return article;
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

        public Columns title() {
            columns.add("title");
            return this;
        }

        public Columns link() {
            columns.add("link");
            return this;
        }

        public Columns text() {
            columns.add("text");
            return this;
        }

        public Columns creationDate() {
            columns.add("creation_date");
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
