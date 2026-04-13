package org.project.sohwagi.domain;

import java.util.Optional;

public interface AppleCredentialRepository {

  void save(AppleCredential appleCredential);

  Optional<AppleCredential> findByOauthSubject(String oauthSubject);

  void delete(AppleCredential appleCredential);

  Optional<AppleCredential> findByUserId(Long userId);
}
