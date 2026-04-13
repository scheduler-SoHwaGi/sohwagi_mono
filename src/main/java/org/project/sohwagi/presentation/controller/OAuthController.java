package org.project.sohwagi.presentation.controller;

import java.util.List;
import org.project.sohwagi.application.cmd.AppleLoginCommand;
import org.project.sohwagi.application.cmd.DeleteUserCommand;
import org.project.sohwagi.application.facade.OAuthService;
import org.project.sohwagi.common.UserInfo;
import org.project.sohwagi.domain.UserDetails;
import org.project.sohwagi.infra.apple.AppleLoginRequest;
import org.project.sohwagi.presentation.res.LoginRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

  private final OAuthService oAuthService;

  public OAuthController(OAuthService oAuthService) {
    this.oAuthService = oAuthService;
  }

  @PostMapping("/apple/login")
  public ResponseEntity<?> appleLogin(@RequestBody AppleLoginRequest request) {

    AppleLoginCommand command = AppleLoginCommand
        .builder()
        .authorizationCode(request.authorizationCode())
        .userName(request.userName())
        .build();

    LoginRes loginRes = oAuthService.appleLogin(command);

    return ResponseEntity.ok(loginRes);
  }

  @DeleteMapping("/apple/revoke")
  public ResponseEntity<String> deleteAppleUser(@RequestHeader("X-REFRESH-TOKEN") String refreshToken,
      @UserInfo UserDetails userDetails) {

    DeleteUserCommand deleteUserCommand = DeleteUserCommand
        .builder()
        .userDetails(userDetails)
        .refreshToken(refreshToken)
        .build();

    oAuthService.deleteAppleUser(deleteUserCommand);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/test")
  public ResponseEntity<?> testLogin(@RequestBody String name) {
    List<String> res = oAuthService.testLogin(name);

    return ResponseEntity.ok(res);
  }

  @PostMapping("/qa")
  public ResponseEntity<LoginRes> qaLogin() {
    LoginRes loginRes = oAuthService.qaLogin();

    return ResponseEntity.ok().body(loginRes);
  }

}
