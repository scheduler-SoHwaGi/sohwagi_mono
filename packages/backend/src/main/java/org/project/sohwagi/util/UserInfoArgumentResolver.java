package org.project.sohwagi.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.sohwagi.application.service.UserService;
import org.project.sohwagi.common.UserInfo;
import org.project.sohwagi.domain.UserDetails;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {

  private final JwtUtil jwtUtil;
  private final UserService userService;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(UserInfo.class)
        && UserDetails.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

    Boolean isAccessToken = (Boolean) request.getAttribute("isAccessToken");

    String tokenValue = jwtUtil.getJwtFromRequest(request, isAccessToken);
    String token = jwtUtil.substringToken(tokenValue);

    log.info(isAccessToken.toString());
    if (isAccessToken == null) {
      throw new IllegalArgumentException("Token type not found");
    }

    log.info(token);
    // JWT 파싱을 통해 사용자 정보 추출
    Long userId = jwtUtil.getUserInfoFromToken(token, isAccessToken);
    log.info(userId.toString());

    return userService.loadUserById(userId);
  }

}
