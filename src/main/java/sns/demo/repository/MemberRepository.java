package sns.demo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sns.demo.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public String save(Member member) {
        existsByUsername(member.getUsername()); // 중복 검증 1
        existsByEmail(member.getEmail()); // 중복 검증 2
        em.persist(member);
        return member.getUsername();
    }


    public Optional<Member> findByUsername(String username) {
        return Optional.ofNullable(
                em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getSingleResult());
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    /**
     * 유효성 검사 - 아이디 중복 체크
     */
    private void existsByUsername(String username) {
        try {
            Member duplicatedMember =
                    em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", username)
                    .getSingleResult();
            if (duplicatedMember != null) {
                throw new IllegalStateException("이미 존재하는 아이디입니다.");
            }
        } catch (NoResultException e) {
            return;
        }
    }

    /**
     * 유효성 검사 - 이메일 중복 체크
     */
    private void existsByEmail(String email) throws IllegalStateException, NoResultException {
        try {
            Member duplicatedMember =
                    em.createQuery("select m from Member m where m.email = :email", Member.class)
                            .setParameter("email", email)
                            .getSingleResult();
            if (duplicatedMember != null) {
                throw new IllegalStateException("이미 사용중인 이메일입니다.");
            }
        } catch (NoResultException e) {
            return;
        }
    }
}
