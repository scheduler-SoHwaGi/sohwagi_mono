package org.project.sohwagi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "apple_credentials")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleCredential {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String oauthSubject;

  @Column
  private String appleRefreshToken;

  @Column(unique = true)
  private Long userId;

  private AppleCredential (String oauthSubject, String appleRefreshToken, Long userId) {
    this.oauthSubject = oauthSubject;
    this.appleRefreshToken = appleRefreshToken;
    this.userId = userId;
  }

  public static AppleCredential create(String oauthSubject, String appleRefreshToken, Long userId) {
    return new AppleCredential(oauthSubject, appleRefreshToken, userId);
  }

}
