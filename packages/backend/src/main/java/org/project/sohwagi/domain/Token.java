package org.project.sohwagi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Column(name = "refresh_token_hash", nullable = false)
  private String refreshTokenHash;

  @Column(nullable = false)
  private LocalDateTime expiresAt;

  public boolean isExpired() {
    return expiresAt.isBefore(LocalDateTime.now());
  }

}
