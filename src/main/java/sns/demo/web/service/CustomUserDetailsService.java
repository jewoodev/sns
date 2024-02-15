package sns.demo.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.dto.CustomUserDetails;
import sns.demo.domain.entity.Member;
import sns.demo.domain.repository.MemberRepository;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> userData = memberRepository.findByUsername(username);

        return userData.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));
    }
}
