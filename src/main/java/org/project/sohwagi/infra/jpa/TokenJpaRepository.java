package org.project.sohwagi.infra.jpa;

import java.util.Optional;
import org.project.sohwagi.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenJpaRepository extends JpaRepository<Token, Long> {

  boolean existsByRefreshToken (String refreshToken);

  Optional<Token> findByRefreshToken(String token);

}
