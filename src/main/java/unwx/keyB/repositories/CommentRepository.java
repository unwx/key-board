package unwx.keyB.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwx.keyB.domains.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
