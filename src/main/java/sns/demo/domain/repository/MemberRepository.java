package sns.demo.domain.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sns.demo.domain.entity.MemberEntity;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

    private final EntityManager em;

    public String save(MemberEntity memberEntity) {
        em.persist(memberEntity);
        return memberEntity.getUsername();
    }


    public Optional<MemberEntity> findByUsername(String username) {

        List<MemberEntity> userEntities = em.createQuery("select m from MemberEntity m where m.username = :username", MemberEntity.class)
                .setParameter("username", username)
                .getResultList();
        return userEntities.isEmpty() ? Optional.empty() : Optional.of(userEntities.get(0));
    }

    public List<MemberEntity> findAll() {
        return em.createQuery("select m from MemberEntity m", MemberEntity.class)
                .getResultList();
    }

    /**
     * 유효성 검사 - 아이디 중복 체크
     */
    public void existsByUsername(String username) {

        List<MemberEntity> userEntities = em.createQuery("select m from MemberEntity m where m.username = :username", MemberEntity.class)
                .setParameter("username", username)
                .getResultList();
        if (!userEntities.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    /**
     * 유효성 검사 - 이메일 중복 체크
     */
    public void existsByEmail(String email) {

        List<MemberEntity> userEntities = em.createQuery("select m from MemberEntity m where m.email = :email", MemberEntity.class)
                .setParameter("email", email)
                .getResultList();
        if (!userEntities.isEmpty()) {
                throw new IllegalStateException("이미 사용중인 이메일입니다.");
            }
    }

    public void updatePassword(MemberEntity memberEntity, String newPassword) {
        memberEntity.updatePassword(newPassword);
        em.flush();
    }
}
