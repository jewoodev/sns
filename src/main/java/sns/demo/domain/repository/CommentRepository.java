package sns.demo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sns.demo.domain.entity.Board;
import sns.demo.domain.entity.Comment;
import sns.demo.domain.entity.Member;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoard(Board board);
    List<Comment> findByMember(Member member);
}
