package org.project.sohwagi.application.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import org.project.sohwagi.application.cmd.RefreshTokenCommand;
import org.project.sohwagi.domain.Token;
import org.project.sohwagi.domain.TokenRepository;
import org.project.sohwagi.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  private final TokenRepository tokenRepository;

  public TokenService(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  public String saveToken(RefreshTokenCommand command, Long userId) {
    Token token = Token.builder()
        .userId(userId)
        .refreshTokenHash(hash(command.refreshToken()))
        .expiresAt(LocalDateTime.now().plusDays(JwtUtil.REFRESH_TOKEN_VALID_DAYS))
        .build();

    tokenRepository.save(token);
    return command.refreshToken();
  }

  public boolean checkToken(String refreshToken) {
    return tokenRepository.checkRefreshToken(hash(refreshToken));
  }

  public void expireToken(RefreshTokenCommand command) {
    tokenRepository.deleteByRefreshTokenHash(hash(command.refreshToken()));
  }

  public void expireAllTokensForUser(Long userId) {
    tokenRepository.deleteAllByUserId(userId);
  }

  public void purgeExpiredTokens() {
    tokenRepository.deleteAllExpiredBefore(LocalDateTime.now());
  }

  private String hash(String rawToken) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hashBytes);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 알고리즘을 사용할 수 없습니다.", e);
    }
  }

}
