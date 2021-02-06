package unwx.keyB.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwx.keyB.domains.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {}
