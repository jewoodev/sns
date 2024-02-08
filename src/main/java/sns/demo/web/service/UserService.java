package sns.demo.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.entity.UserEntity;
import sns.demo.domain.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional
    public String join(UserEntity userEntity) {
        memberRepository.existsByUsername(userEntity.getUsername()); // 중복 검증 1
        memberRepository.existsByEmail(userEntity.getEmail()); // 중복 검증 2

        memberRepository.save(userEntity);
        return userEntity.getUsername();
    }

    /**
     * 단일 회원 조회
     */
    public UserEntity findByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }

    /**
     * 전체 회원 조회
     */
    public List<UserEntity> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public String updateMemberPassword(UserEntity userEntity, String newPassword) {
        userEntity.updatePassword(newPassword);
        memberRepository.save(userEntity);

        return userEntity.getUsername();
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
}
