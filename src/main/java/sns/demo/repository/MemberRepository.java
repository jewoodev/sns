package sns.demo.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sns.demo.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository{

    private final EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getMemberId();
    }


    public Optional<Member> findById(Long memberId) {
        return Optional.ofNullable(em.find(Member.class, memberId));
    }

    public List<Member> findByName(String username) {
        return em.createQuery(
                "select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }


    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }


    public void delete() {

    }
}
