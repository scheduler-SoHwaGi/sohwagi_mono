package org.project.sohwagi.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.project.sohwagi.common.TokenValidationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtUtil {

  public static final String AUTHORIZATION_HEADER = "X-ACCESS-TOKEN";
  public static final String BEARER_PREFIX = "Bearer ";
  private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

  private final String INVALID_JWT_SIGNATURE = "Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.";
  private final String EXPIRED_JWT_TOKEN = "Expired JWT token, 만료된 JWT token 입니다.";
  private final String UNSUPPORTED_JWT_TOKEN = "Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.";
  private final String EMPTY_JWT_CLAIMS = "JWT claims is empty, 잘못된 JWT 토큰 입니다.";
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  @Value("${jwt.access.secret.key}") // Base64 Encode 한 SecretKey
  private String accessSecretKey;

  @Value("${jwt.refresh.secret.key}")
  private String refreshSecretKey;

  private Key accessKey;
  private Key refreshKey;

  @PostConstruct
  public void init() {
    byte[] accessKeyBytes = Base64.getDecoder().decode(accessSecretKey);
    accessKey = Keys.hmacShaKeyFor(accessKeyBytes); // Access Token용 Key 초기화

    byte[] refreshKeyBytes = Base64.getDecoder().decode(refreshSecretKey);
    refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes); // Refresh Token용 Key 초기화

  }

  public String createAccessToken(Long userId) {
    Date date = new Date();

    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(String.valueOf(userId)) // 사용자 식별자값(ID)
            .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
            .setIssuedAt(date) // 발급일
            .signWith(accessKey, signatureAlgorithm) // 암호화 알고리즘
            .compact();
  }

  public String createRefreshToken(Long userId) {
    Date date = new Date();
    Date expiration = new Date(date.getTime() + Duration.ofDays(30).toMillis());

    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(String.valueOf(userId)) // 사용자 식별자값(ID)
            .setExpiration(expiration) // 만료 시간
            .setIssuedAt(date) // 발급일
            .signWith(refreshKey, signatureAlgorithm) // 암호화 알고리즘
            .compact();
  }

  public Long getUserInfoFromToken(String token, boolean isAccessToken) {
    Key key = isAccessToken ? accessKey : refreshKey;

    log.info("getUserInfo");

    String userId = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()
        .getSubject();

    log.info(userId);
    return Long.valueOf(userId);
  }

  public String substringToken(String tokenValue) {
    log.info("tokenvalue : " + tokenValue);
    if (!StringUtils.hasText(tokenValue) || !tokenValue.startsWith(BEARER_PREFIX)) {
      throw new JwtException(INVALID_JWT_SIGNATURE);
    }

    return tokenValue.substring(7);
  }

  public String getJwtFromRequest(HttpServletRequest request, boolean isAccessToken) {
    String headerName = isAccessToken ? "X-ACCESS-TOKEN" : "X-REFRESH-TOKEN";
    return request.getHeader(headerName);
  }

  public TokenValidationResult validateToken(String token, boolean isAccessToken) {
    try {
      Key key = isAccessToken ? accessKey : refreshKey;

      log.info("validate 토큰");
      token = substringToken(token);

      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);

      return TokenValidationResult.VALID;// 토큰 유효

    } catch (ExpiredJwtException e) {
      return TokenValidationResult.EXPIRED; // 만료된 토큰
    } catch (JwtException e) {
      return TokenValidationResult.INVALID; // 잘못된 토큰
    }
  }

}
