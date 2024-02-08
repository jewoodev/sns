package sns.demo.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sns.demo.web.interceptor.LogInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**", "/*.ico", "/error");

//        registry.addInterceptor(new LoginCheckInterceptor())
//                .order(2)
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/", "/members/new", "/login", "/logout",
//                        "/static/**", "/x.ico", "/error"
//                );
    }

    //    @Override
    //    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    //        resolvers.add(new LoginMemberArgumentResolver());
    //    }


}
