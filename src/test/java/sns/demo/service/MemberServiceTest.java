package sns.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.entity.Member;
import sns.demo.domain.repository.MemberRepository;
import sns.demo.web.service.MemberService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void join() {
        Member member = Member.builder()
                .username("신제우")
                .password("1234")
                        .build();
        member.updatePassword("4567");

        String username = memberService.join(member);

        assertEquals(member, memberRepository.findByUsername(username).get());
    }

    /**
     * 유저네임 중복 테스트
     */
    @Test
    public void validDuplMember() {
        Member member1 = Member.builder()
                        .username("신제우")
                                .password("1219")
                                        .build();

        Member member2 = Member.builder()
                        .username("신제우")
                                .password("1219")
                                        .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            memberService.join(member1);
            memberService.join(member2);
        });
    }
}