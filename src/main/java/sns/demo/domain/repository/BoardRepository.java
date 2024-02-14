package sns.demo.domain.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sns.demo.domain.dto.board.BoardRequestDTO;
import sns.demo.domain.entity.Board;
import sns.demo.domain.entity.File;
import sns.demo.domain.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepository {
    private final EntityManager em;

    public Long save(Board board) {
        em.persist(board);
        return board.getId();
    }

    public Optional<Board> findById(Long boardId) {
        return Optional.ofNullable(em.find(Board.class, boardId));
    }

    public List<Board> findAll() {
        return em.createQuery("select b from Board b order by b.likeCount desc, b.viewCount desc", Board.class).getResultList();
    }

    public void delete(Board board) {
        Board foundBoard = em.find(Board.class, board.getId());
        em.remove(foundBoard);
    }

    public List<Board> findByMember(Member member) {
        return em.createQuery("select b from Board b where b.member = :member", Board.class)
                .setParameter("member", member)
                .getResultList();
    }

    public void update(Long id, BoardRequestDTO dto, List<File> boardImages) {
        Board foundBoard = em.find(Board.class, id);
        foundBoard.update(dto, boardImages);
    }


    public void increaseViews(Board board) {
        board.increaseViews();
    }

    public void increaseLikes(Board board) {
        board.increaseLikes();
    }

    public void decreaseLikes(Board board) {
        board.decreaseLikes();
    }
}
