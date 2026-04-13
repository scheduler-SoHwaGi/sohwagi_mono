package org.project.sohwagi.infra.jpa;

import java.util.Optional;
import org.project.sohwagi.domain.AppleCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppleCredentialJpaRepository extends JpaRepository<AppleCredential, Long> {

  Optional<AppleCredential> findByOauthSubject(String oauthSubject);

  Optional<AppleCredential> findByUserId(Long userId);
}
