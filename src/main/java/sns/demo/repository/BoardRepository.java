package sns.demo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import sns.demo.domain.Board;

import java.util.List;
import java.util.Optional;

@Repository
public class BoardRepository{
    @PersistenceContext
    EntityManager em;

    public Long save(Board board) {
        em.persist(board);
        return board.getBoardId();
    }

    public Optional<Board> findById(Long boardId) {
        return Optional.ofNullable(em.find(Board.class, boardId));
    }

    public List<Board> findAll() {
        return null;
    }

    public void delete() {

    }
}
