package sns.demo.web.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.Member;
import sns.demo.domain.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     * @throws IllegalStateException
     */
    @Transactional
    public String join(Member member) throws IllegalStateException{
        memberRepository.save(member);
        return member.getUsername();
    }

    /**
     * 단일 회원 조회
     * @param username
     * @return Member
     */
    public Member findByUsername(String username) {
        return memberRepository.findMemberByUsername(username).orElse(null);
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 로그인
     * @return null이면 로그인 실패
     */
    //로그인
    public Member login(String username, String password) {
        return memberRepository.findMemberByUsername(username)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }

    /**
     * 비밀번호 변경
     * @param member
     * @param newPassword
     * @return
     */
    @Transactional
    public String updateMemberPassword(Member member, String newPassword) {
        member.updatePassword(newPassword);
        memberRepository.save(member);

        return member.getUsername();
    }
}
