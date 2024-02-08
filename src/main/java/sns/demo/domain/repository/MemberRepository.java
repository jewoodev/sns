package sns.demo.domain.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sns.demo.domain.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

    private final EntityManager em;

    public String save(UserEntity userEntity) {
        em.persist(userEntity);
        return userEntity.getUsername();
    }


    public Optional<UserEntity> findByUsername(String username) {

        List<UserEntity> userEntities = em.createQuery("select m from UserEntity m where m.username = :username", UserEntity.class)
                .setParameter("username", username)
                .getResultList();
        return userEntities.isEmpty() ? Optional.empty() : Optional.of(userEntities.get(0));
    }

    public List<UserEntity> findAll() {
        return em.createQuery("select m from UserEntity m", UserEntity.class)
                .getResultList();
    }

    /**
     * 유효성 검사 - 아이디 중복 체크
     */
    public void existsByUsername(String username) {

        List<UserEntity> userEntities = em.createQuery("select m from UserEntity m where m.username = :username", UserEntity.class)
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

        List<UserEntity> userEntities = em.createQuery("select m from UserEntity m where m.email = :email", UserEntity.class)
                .setParameter("email", email)
                .getResultList();
        if (!userEntities.isEmpty()) {
                throw new IllegalStateException("이미 사용중인 이메일입니다.");
            }
    }
}
