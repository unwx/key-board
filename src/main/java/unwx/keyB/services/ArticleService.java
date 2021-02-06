package unwx.keyB.services;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unwx.keyB.domains.Article;
import unwx.keyB.repositories.ArticleRepository;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Nullable
    public List<Article> getAll(){
        return articleRepository.findAll();
    }

    public Article getById(Long id) {
        return articleRepository.findById(id).orElseGet(Article::new);
    }

    public Article save(@NotNull Article article){
        return articleRepository.save(article);
    }

    public void delete(@NotNull Article article){
        articleRepository.delete(article);
    }
}
