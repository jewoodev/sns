package sns.demo.configure;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import sns.demo.domain.Role;

//@Configuration
//@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됩니다.
//public class SecurityConfig {
//
//    private static final String[] AUTH_WHITELIST = {
//            "/", "/static"
//    };
//
//    // 해당 메서드에서 리턴되는 오브젝트를 IoC로 등록해준다
//    @Bean
//    public BCryptPasswordEncoder encodePwd() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable
//                )
//                .authorizeHttpRequests((authorizeRequests) ->
//                        authorizeRequests
//                                .requestMatchers("/user/**").authenticated()
//                                .requestMatchers("/admin/**").hasRole(Role.USER.name())
//                                .anyRequest().permitAll())
//                .formLogin(form -> form
//                        .loginPage("/login/loginForm")
//                        .loginProcessingUrl("/login") // form action으로 "/login" 이 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
//                        .defaultSuccessUrl("/"));
//                return http.build();
//    }
//}
