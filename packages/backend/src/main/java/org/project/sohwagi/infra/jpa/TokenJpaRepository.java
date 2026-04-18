package org.project.sohwagi.infra.jpa;

import java.time.LocalDateTime;
import java.util.Optional;
import org.project.sohwagi.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenJpaRepository extends JpaRepository<Token, Long> {

  Optional<Token> findByRefreshTokenHash(String refreshTokenHash);

  void deleteByRefreshTokenHash(String refreshTokenHash);

  void deleteAllByUserId(Long userId);

  void deleteAllByExpiresAtBefore(LocalDateTime time);

}
