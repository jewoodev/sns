package sns.demo.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sns.demo.domain.dto.CustomUserDetails;
import sns.demo.domain.entity.UserEntity;
import sns.demo.domain.repository.MemberRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userData = memberRepository.findByUsername(username);

        return userData.map(CustomUserDetails::new).orElse(null);
    }
}
