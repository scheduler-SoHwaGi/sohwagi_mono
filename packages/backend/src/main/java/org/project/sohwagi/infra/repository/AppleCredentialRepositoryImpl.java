package org.project.sohwagi.infra.repository;

import java.util.Optional;
import org.project.sohwagi.domain.AppleCredential;
import org.project.sohwagi.domain.AppleCredentialRepository;
import org.project.sohwagi.infra.jpa.AppleCredentialJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AppleCredentialRepositoryImpl implements AppleCredentialRepository {

  private final AppleCredentialJpaRepository appleCredentialJpaRepository;

  public AppleCredentialRepositoryImpl(AppleCredentialJpaRepository appleCredentialJpaRepository) {
    this.appleCredentialJpaRepository = appleCredentialJpaRepository;
  }

  @Override
  public void save(AppleCredential appleCredential) {
    appleCredentialJpaRepository.save(appleCredential);
  }

  @Override
  public Optional<AppleCredential> findByOauthSubject(String oauthSubject) {
    return appleCredentialJpaRepository.findByOauthSubject(oauthSubject);
  }

  @Override
  public void delete(AppleCredential appleCredential) {
    appleCredentialJpaRepository.delete(appleCredential);
  }

  @Override
  public Optional<AppleCredential> findByUserId(Long userId) {
    return appleCredentialJpaRepository.findByUserId(userId);
  }
}
