package sns.demo.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.entity.Member;
import sns.demo.domain.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

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
    public Optional<Member> findById(Long id) {return memberRepository.findById(id);}

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
        memberRepository.updatePassword(memberParam.getId(), newPassword);
    }

    public void existsByEmail(String email) {
        memberRepository.existsByEmail(email);
    }

    public void existByUsername(String username) {
        memberRepository.existsByUsername(username);
    }

    @Transactional
    public void delete(Member member) {
        memberRepository.delete(member.getId());
    }
}
