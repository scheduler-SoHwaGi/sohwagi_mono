package org.project.sohwagi.presentation;

import lombok.extern.slf4j.Slf4j;
import org.project.sohwagi.application.service.TokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenCleanupScheduler {

  private final TokenService tokenService;

  public TokenCleanupScheduler(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul")
  public void purgeExpiredTokens() {
    log.info("[TokenCleanupScheduler] purge expired refresh tokens start");
    tokenService.purgeExpiredTokens();
  }
}
