package sns.demo.web.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.entity.Member;
import sns.demo.domain.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final EntityManager em;

    /**
     * 회원가입
     */
    @Transactional
    public String join(Member member) {
        memberRepository.save(member);
        return member.getUsername();
    }

    /**
     * 단일 회원 조회
     */
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void updateMemberPassword(Member memberParam, String newPassword) {
        Member findMember = em.find(Member.class, memberParam.getId());
        memberRepository.updatePassword(findMember, newPassword);
    }

//    /**
//     * 로그인
//     * @return null이면 로그인 실패
//     */
//    public Member login(String username, String password) {
//        return memberRepository.findByUsername(username)
//                .filter(m -> m.getPassword().equals(password))
//                .orElse(null);
//    }

    public void existsByEmail(String email) {
        memberRepository.existsByEmail(email);
    }

    public void existByUsername(String username) {
        memberRepository.existsByUsername(username);
    }
}
