package sns.demo.web.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import sns.demo.domain.Role;
import sns.demo.web.auth.CustomAuthFailureHandler;
import sns.demo.web.service.CustomUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됩니다.
public class SecurityConfig {

    private final CustomAuthFailureHandler failureHandler;


    // 해당 메서드에서 리턴되는 오브젝트를 IoC로 등록해준다
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService userDetailsService,
                                                       BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);

        return providerManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable
                )
                .httpBasic(AbstractHttpConfigurer::disable
                )
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/", "/login", "/error/**",
                                        "/bootstrap.min.css", "/style.css", "/favicon/**",
                                        "/members/new").permitAll()
                                .requestMatchers("/board/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                                .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                                .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .formLogin(loginFail -> loginFail
                        .failureHandler(failureHandler))
                .logout(withDefaults());

        return http.build();
    }

}
