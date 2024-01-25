package sns.demo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import sns.demo.domain.Member;
import sns.demo.repository.MemberRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    //회원가입
    @Transactional
    public Long join(Member member) throws IllegalStateException{
        validateDuplicateMember(member); //중복 검증
        memberRepository.save(member);
        return member.getMemberId();
    }

    private void validateDuplicateMember(Member member) throws IllegalStateException, NoResultException {
        try {
            Member duplicatedMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", member.getUsername())
                    .getSingleResult();
            if (duplicatedMember != null) {
                throw new IllegalStateException("이미 존재하는 아이디입니다.");
            }
        } catch (NoResultException e) {
            return;
        }
    }

    //단일 회원 조회
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).get();
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
    public Long updateMemberPassword(Member member, String newPassword) {
        member.updatePassword(newPassword);
        memberRepository.saveAndFlush(member);

        return member.getMemberId();
    }
}
