package sns.demo.domain.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sns.demo.domain.entity.BoardEntity;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepository {
    private final EntityManager em;

    public Long save(BoardEntity boardEntity) {
        em.persist(boardEntity);
        return boardEntity.getId();
    }

    public Optional<BoardEntity> findById(Long boardId) {
        return Optional.ofNullable(em.find(BoardEntity.class, boardId));
    }

    public List<BoardEntity> findAll() {
        return em.createQuery("select b from BoardEntity b", BoardEntity.class).getResultList();
    }

}
