package org.project.sohwagi.application.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.project.sohwagi.application.cmd.AppleLoginCommand;
import org.project.sohwagi.application.cmd.DeleteUserCommand;
import org.project.sohwagi.application.cmd.RefreshTokenCommand;
import org.project.sohwagi.application.service.AppleCredentialService;
import org.project.sohwagi.application.service.ScheduleService;
import org.project.sohwagi.application.service.TokenService;
import org.project.sohwagi.application.service.UserService;
import org.project.sohwagi.domain.AppleCredential;
import org.project.sohwagi.domain.User;
import org.project.sohwagi.domain.UserDetails;
import org.project.sohwagi.infra.apple.AppleClient;
import org.project.sohwagi.infra.apple.AppleOAuthInfoRes;
import org.project.sohwagi.presentation.res.LoginRes;
import org.project.sohwagi.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuthService {

  private final AppleClient appleClient;
  private final UserService userService;
  private final TokenService tokenService;
  private final JwtUtil jwtUtil;
  private final ScheduleService scheduleService;
  private final AppleCredentialService appleCredentialService;

  public OAuthService(
      AppleClient appleClient,
      JwtUtil jwtUtil,
      UserService userService,
      TokenService tokenService,
      ScheduleService scheduleService,
      AppleCredentialService appleCredentialService) {
    this.appleClient = appleClient;
    this.jwtUtil = jwtUtil;
    this.userService = userService;
    this.tokenService = tokenService;
    this.scheduleService = scheduleService;
    this.appleCredentialService = appleCredentialService;
  }

  @Transactional
  public LoginRes appleLogin(AppleLoginCommand command) {
    AppleOAuthInfoRes appleOAuthInfoRes = fetchAppleOAuthInfo(command);
    Long userId = resolveUserId(appleOAuthInfoRes, command.userName());
    String accessToken = generateAccessToken(userId);
    String refreshToken = generateAndSaveRefreshToken(userId);
    return buildLoginResponse(accessToken, refreshToken);
  }

  @Transactional
  public void deleteAppleUser(DeleteUserCommand deleteUserCommand) {
    revokeAppleAccess(deleteUserCommand.userDetails());
    cleanupUserData(deleteUserCommand.userDetails(), deleteUserCommand.refreshToken());
  }

  public List<String> testLogin(String name) {
    List<String> res = new ArrayList<>();
    User user = userService.saveUser(name, "test@gmail.com", "TEST");

    String accessToken = generateAccessToken(user.getId());
    String refreshToken = generateAndSaveRefreshToken(user.getId());

    res.add(accessToken);
    res.add(refreshToken);

    return res;
  }

  @Transactional
  public LoginRes qaLogin() {
    User user = userService.getTestUser("TestUser");

    String accessToken = generateAccessToken(user.getId());
    String refreshToken = generateAndSaveRefreshToken(user.getId());

    return LoginRes.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  private AppleOAuthInfoRes fetchAppleOAuthInfo(AppleLoginCommand appleLoginCommand) {
    return appleClient.getAppleOAuthInfo(appleLoginCommand);
  }

  private Long resolveUserId(AppleOAuthInfoRes appleOAuthInfoRes, String userName) {
    return findExistingUserId(appleOAuthInfoRes.subject())
        .orElseGet(() -> createNewUser(appleOAuthInfoRes, userName));
  }

  private LoginRes buildLoginResponse(String accessToken, String refreshToken) {
    return LoginRes.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  private Optional<Long> findExistingUserId(String subject) {
    return appleCredentialService.findAppleCredential(subject).map(AppleCredential::getUserId);
  }

  private Long createNewUser(AppleOAuthInfoRes oAuthInfo, String userName) {
    User newUser = userService.saveUser(userName, "APPLE", oAuthInfo.email());
    appleCredentialService.saveAppleCredential(
        oAuthInfo.subject(), oAuthInfo.refreshToken(), newUser.getId());
    return newUser.getId();
  }

  private String generateAccessToken(Long userId) {
    return jwtUtil.createAccessToken(userId);
  }

  private String generateAndSaveRefreshToken(Long userId) {
    String refreshToken = jwtUtil.createRefreshToken(userId);

    RefreshTokenCommand command = new RefreshTokenCommand(refreshToken);
    return tokenService.saveToken(command);
  }

  private void revokeAppleAccess(UserDetails userDetails) {
    AppleCredential appleCredential = appleCredentialService.getAppleCredentialByUserId(
        userDetails.id());
    appleClient.appleRevoke(appleCredential.getAppleRefreshToken());
    appleCredentialService.deleteAppleCredential(appleCredential);
  }

  private void cleanupUserData(UserDetails userDetails, String refreshToken) {
    RefreshTokenCommand tokenCommand = RefreshTokenCommand.builder()
        .refreshToken(refreshToken)
        .build();
    tokenService.expireToken(tokenCommand);

    Long userId = userDetails.id();
    scheduleService.deleteScheduleByUserRevoke(userId);
    userService.deleteUser(userDetails);
  }
}
