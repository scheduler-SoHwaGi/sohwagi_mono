package org.project.sohwagi.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.sohwagi.application.cmd.RefreshTokenCommand;
import org.project.sohwagi.application.cmd.SaveFcmTokenCommand;
import org.project.sohwagi.application.service.TokenService;
import org.project.sohwagi.application.service.UserService;
import org.project.sohwagi.common.UserInfo;
import org.project.sohwagi.domain.UserDetails;
import org.project.sohwagi.presentation.req.PostFcmTokenReq;
import org.project.sohwagi.presentation.res.GetUserInfoRes;
import org.project.sohwagi.presentation.res.OnboardingCheckResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final TokenService tokenService;

  @PostMapping("/fcmTokens")
  public ResponseEntity<String> saveFcmToken(@RequestBody PostFcmTokenReq request,
      @UserInfo UserDetails userDetails) {
    SaveFcmTokenCommand command = SaveFcmTokenCommand
        .builder()
        .fcmToken(request.getFcmToken())
        .userDetails(userDetails)
        .build();

    userService.saveFcmToken(command);

    return ResponseEntity.ok().build();
  }

  @PatchMapping("/logout")
  public ResponseEntity<String> logout(@RequestHeader("X-REFRESH-TOKEN") String refreshToken) {

    RefreshTokenCommand refreshTokenCommand = RefreshTokenCommand
        .builder().refreshToken(refreshToken).build();

    tokenService.expireToken(refreshTokenCommand);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/me")
  public ResponseEntity<GetUserInfoRes> getUserInfo(@UserInfo
  UserDetails userDetails) {
    GetUserInfoRes getUserInfoRes = userService.getUserInfo(userDetails);

    return ResponseEntity.ok(getUserInfoRes);
  }

  @GetMapping("/me/onboarding")
  public ResponseEntity<OnboardingCheckResponse> checkOnboarding(
      @UserInfo UserDetails userDetails) {
    return ResponseEntity.ok(new OnboardingCheckResponse(userDetails.hasSchedule()));
  }

}
