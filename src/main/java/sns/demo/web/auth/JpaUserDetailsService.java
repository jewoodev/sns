package sns.demo.web.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sns.demo.domain.Member;
import sns.demo.domain.repository.MemberRepository;

import java.util.List;
import java.util.function.Supplier;

/**
 * 시큐리티 설정에서 `loginProcessingUrl("/login")` 으로 지정된 요청이 오면
 * 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 함수가 실행된다
 */

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 1. 뷰에서 사용하는 username의 네이밍이 "username"과 다른 것으로 하고 싶으면
     * SecurityConfig에서 `setUsernameParameter()` 메서드를 사용하자
     * 2. 이 메서드로 UserDetails가 리턴되면 Authentication 내부에 UserDetails 타입 객체가 쏘옥 들어가게 된다
     * 3. 그 후 Authentication이 시큐리티 session에 쏘옥 들어가게 된다
     */
    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Supplier<UsernameNotFoundException> s =
                () -> new UsernameNotFoundException("Problem during authentication");

        Member m = memberRepository.findMemberByUsername(username).orElseThrow(s);
        return new CustomUserDetails(m);
    }
}
