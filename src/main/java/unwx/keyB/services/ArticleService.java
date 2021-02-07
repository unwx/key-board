package unwx.keyB.services;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;
import unwx.keyB.domains.Article;
import unwx.keyB.repositories.ArticleRepository;
import unwx.keyB.services.auxiliary.Transliterator;

import java.time.LocalDateTime;
import java.util.List;

@Service
@PropertySources({
        @PropertySource("classpath:valid.properties"),
        @PropertySource("classpath:uri.properties")
})
public class ArticleService {

    @Value("${article.title.maxlength}")
    private Short MAX_TITLE_LENGTH;

    @Value("${article.title.minlength}")
    private Short MIN_TITLE_LENGTH;

    private final ArticleRepository articleRepository;

    /* for transliterate from title to link. */
    private final Transliterator transliterator;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, Transliterator transliterator) {
        this.articleRepository = articleRepository;
        this.transliterator = transliterator;
    }

    @Nullable
    public List<Article> getAll(){
        return articleRepository.findAll();
    }

    public Article getById(Long id) {
        return articleRepository.findById(id).orElseGet(Article::new);
    }

    public Article save(@NotNull Article article){
        article.setLink(transliterator.transliterate(article.getTitle(), MIN_TITLE_LENGTH, MAX_TITLE_LENGTH));
        article.setCreationDate(LocalDateTime.now());
        return articleRepository.save(article);
    }

    public void delete(@NotNull Article article){
        articleRepository.delete(article);
    }

}
