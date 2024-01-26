package sns.demo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.Member;
import sns.demo.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl {

    private final MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    //회원가입
    @Transactional
    public String join(Member member) throws IllegalStateException{
        return memberRepository.save(member);
    }

    //단일 회원 조회
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }

    //전체 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * @return null이면 로그인 실패
     */
    //로그인
    public Member login(String username, String password) {
        return memberRepository.findByUsername(username)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }

    @Transactional
    public String updateMemberPassword(Member member, String newPassword) {
        member.updatePassword(newPassword);
        memberRepository.save(member);

        return member.getUsername();
    }
}
