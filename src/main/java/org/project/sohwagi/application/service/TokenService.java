package org.project.sohwagi.application.service;

import org.project.sohwagi.application.cmd.RefreshTokenCommand;
import org.project.sohwagi.domain.Token;
import org.project.sohwagi.domain.TokenRepository;
import org.project.sohwagi.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  private final JwtUtil jwtUtil;
  private final TokenRepository tokenRepository;

  public TokenService(TokenRepository tokenRepository, JwtUtil jwtUtil){
    this.tokenRepository = tokenRepository;
    this.jwtUtil = jwtUtil;
  }

  public String saveToken(RefreshTokenCommand command){

    Token token = Token.builder()
        .refreshToken(command.refreshToken())
        .isExpired(false)
        .build();

    Token savedToken = tokenRepository.save(token);

    return savedToken.getRefreshToken();
  }

  public boolean checkToken(String refreshToken) {
    return tokenRepository.checkRefreshToken(refreshToken);
  }

  public void expireToken(RefreshTokenCommand command){
    Token token = tokenRepository.findByRefreshToken(command);

    token.expireToken();

    tokenRepository.update(token);
  }

}
