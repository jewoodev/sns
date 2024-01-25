package sns.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.Member;
import sns.demo.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberServiceImpl memberServiceImpl;
    @Autowired MemberRepository memberRepository;

    @Test
    void join() {
        Member member = new Member(null, "신제우", "1234", "asd@naver.com");
        member.updatePassword("4567");

        Long saveId = memberServiceImpl.join(member);

        assertEquals(member, memberRepository.findById(saveId).get());
    }

    @Test
    public void validDuplMember() {
//        Member member1 = new Member();
//        member1.setUsername("신제우");
//        member1.setPassword("1219");
//
//        Member member2 = new Member();
//        member2.setUsername("신제우");
//        member2.setPassword("1219");

//        assertThrows(IllegalStateException.class, () -> {
//            memberServiceImpl.join(member1);
//            memberServiceImpl.join(member2);
//        });
    }


    @Test
    void findOne() {
    }

    @Test
    void findMembers() {
    }
}