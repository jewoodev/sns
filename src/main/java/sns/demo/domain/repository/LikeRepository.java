package sns.demo.domain.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sns.demo.domain.entity.Board;
import sns.demo.domain.entity.Likes;
import sns.demo.domain.entity.Member;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class LikeRepository {

    private final EntityManager em;

    public Likes save(Likes likes) {
        em.persist(likes);
        return likes;
    }

    public Optional<Likes> findByBoardAndMember(Board board, Member member) {
        List<Likes> likesList = em.createQuery(
                "select l from Likes l where l.board = :board and l.member = :member", Likes.class)
                .setParameter("board", board)
                .setParameter("member", member)
                .getResultList();
        return likesList.isEmpty() ? Optional.empty() : Optional.of(likesList.get(0));
    }

    public void changeLike(Likes likes) {
        Likes findOne = em.find(Likes.class, likes.getId());
        findOne.changeLike();
    }
}
