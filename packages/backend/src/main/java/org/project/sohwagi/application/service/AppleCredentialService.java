package org.project.sohwagi.application.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.project.sohwagi.domain.AppleCredential;
import org.project.sohwagi.domain.AppleCredentialRepository;
import org.springframework.stereotype.Service;

@Service
public class AppleCredentialService {

  private final AppleCredentialRepository appleCredentialRepository;

  public AppleCredentialService(AppleCredentialRepository appleCredentialRepository) {
    this.appleCredentialRepository = appleCredentialRepository;
  }

  public void saveAppleCredential(String oauthSubject, String appleRefreshToken, Long userId) {
    AppleCredential appleCredential = AppleCredential.create(
        oauthSubject, appleRefreshToken, userId);
    appleCredentialRepository.save(appleCredential);
  }

  public Optional<AppleCredential> findAppleCredential(String oauthSubject) {
    return appleCredentialRepository.findByOauthSubject(oauthSubject);
  }

  public void deleteAppleCredential(AppleCredential appleCredential) {
    appleCredentialRepository.delete(appleCredential);
  }

  public AppleCredential getAppleCredentialByUserId(Long userId) {
    return appleCredentialRepository
        .findByUserId(userId)
        .orElseThrow(() -> new EntityNotFoundException("Apple credential not found"));
  }
}
