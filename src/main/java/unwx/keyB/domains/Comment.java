package unwx.keyB.domains;

import javax.persistence.*;

@Entity
@Table
@SuppressWarnings("unused")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "text", length = 1024)
    private String text;

    @Column(name = "likes", nullable = false)
    private int likes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Comment(String text,
                   int likes,
                   Article article,
                   User author) {
        this.text = text;
        this.likes = likes;
        this.article = article;
        this.author = author;
    }

    public Comment() {
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
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

        public Builder likes(int likes) {
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
}
