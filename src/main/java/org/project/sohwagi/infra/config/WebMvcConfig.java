package org.project.sohwagi.infra.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.sohwagi.util.LoginInterceptor;
import org.project.sohwagi.util.UserInfoArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  private final LoginInterceptor loginInterceptor;
  private final UserInfoArgumentResolver userInfoArgumentResolver;

  public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(userInfoArgumentResolver);
  }

  public void addInterceptors(InterceptorRegistry registry) {
    log.info("인터셉터 등록");
    registry.addInterceptor(loginInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns("/oauth/apple/login", "/error", "/oauth/test", "/", "/manager/**",
            "/users/logout", "/oauth/qa", "/api-docs", "/swagger-ui.html",
            "/swagger-ui/**", "/v3/api-docs/**");
  }
}
