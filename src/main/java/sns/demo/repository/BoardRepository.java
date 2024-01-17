package sns.demo.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sns.demo.domain.Board;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepository {
    private final EntityManager em;

    public Long save(Board board) {
        em.persist(board);
        return board.getBoardId();
    }

    public Optional<Board> findById(Long boardId) {
        return Optional.ofNullable(em.find(Board.class, boardId));
    }

    public List<Board> findAll() {
        return em.createQuery("select b from Board b", Board.class).getResultList();
    }

}
