package sns.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sns.demo.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    /**
     * 유효성 검사 - 중복 체크
     * @param email 회원 이메일
     * @return
     */
    boolean existsByEmail(String email);

    Optional<Member> findByUsername(String username);
}
