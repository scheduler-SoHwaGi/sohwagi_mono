package org.project.sohwagi.domain;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.project.sohwagi.common.PersistenceAdapter;
import org.project.sohwagi.common.exception.RefreshTokenException;
import org.project.sohwagi.infra.jpa.TokenJpaRepository;

@PersistenceAdapter
@RequiredArgsConstructor
public class TokenRepository {

  private final TokenJpaRepository tokenJpaRepository;

  public boolean checkRefreshToken(String refreshTokenHash) {
    Token token = tokenJpaRepository.findByRefreshTokenHash(refreshTokenHash)
        .orElseThrow(() -> new RefreshTokenException("해당 RefreshToken이 DB 내 존재하지 않습니다."));

    return token.isExpired();
  }

  public Token save(Token token) {
    return tokenJpaRepository.save(token);
  }

  public void deleteByRefreshTokenHash(String refreshTokenHash) {
    tokenJpaRepository.deleteByRefreshTokenHash(refreshTokenHash);
  }

  public void deleteAllByUserId(Long userId) {
    tokenJpaRepository.deleteAllByUserId(userId);
  }

  public void deleteAllExpiredBefore(LocalDateTime time) {
    tokenJpaRepository.deleteAllByExpiresAtBefore(time);
  }
}
