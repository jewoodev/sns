package sns.demo.web.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sns.demo.domain.Member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 시큐리티가 "/login" 주소 요청이 오면 낚아채서 로그인을 진행시킨다
 * 로그인 진행이 완료되면 session에 시큐리티 session을 만든다 (Security ContextHolder)
 * 시큐리티가 가지고 있는 session에 들어갈 수 있는 오브젝트는 정해져 있다 => Authentication 타입 객체
 * Authentication 안에 User 정보가 있어야 함. User 오브젝트 타입은 UserDetails 타입 객체

 * 시큐리티가 가지고 있는 Security session 영역이 있다,
 * Security Session => Authentication => UserDetails
 */

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member; // 콤포지션


    /**
     * @return 해당 Member의 권한
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
