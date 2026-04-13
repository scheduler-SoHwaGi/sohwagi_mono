package org.project.sohwagi.infra.apple;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.PrivateKey;
import java.security.Security;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.project.sohwagi.application.cmd.AppleLoginCommand;
import org.project.sohwagi.common.exception.OAuthRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleClient {

  private final RestTemplate restTemplate;

  @Value("${apple.signin.client.id}")
  private String clientId;

  @Value("${apple.signin.team.id}")
  private String teamId;

  @Value("${apple.signin.key.id}")
  private String keyId;

  @Value("${apple.signin.audience}")
  private String audience;

  @Value("${apple.signin.private.key}")
  private String privateKey;

  @Value("${apple.signin.grant.type}")
  private String grantType;

  @Transactional
  public AppleOAuthInfoRes getAppleOAuthInfo(AppleLoginCommand command) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.valueOf(APPLICATION_FORM_URLENCODED_VALUE));
    HttpEntity<String> request = new HttpEntity<>("client_id=" + clientId +
        "&client_secret=" + generateClientSecret() +
        "&grant_type=" + grantType +
        "&code=" + command.authorizationCode(), headers);

    ResponseEntity<AppleSocialTokenRes> response;
    try {
      response = restTemplate.exchange(
          audience + "/auth/token",
          HttpMethod.POST,
          request,
          AppleSocialTokenRes.class
      );

      DecodedJWT decodedJWT = JWT.decode(Objects.requireNonNull(response.getBody()).idToken());

      return new AppleOAuthInfoRes(decodedJWT.getClaim("sub").asString(),
          decodedJWT.getClaim("email").asString(), response.getBody().refreshToken());

    } catch (HttpClientErrorException e) {
      log.error("Revoke failed: {}", e.getMessage());
      throw new OAuthRequestException((HttpStatus) e.getStatusCode(),
          e.getMessage());
    }
  }

  public void appleRevoke(String appleRefreshToken) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<String> request = new HttpEntity<>(
        "client_id=" + clientId +
            "&client_secret=" + generateClientSecret() +
            "&token=" + appleRefreshToken,
        headers
    );

    try {
      ResponseEntity<Void> response = restTemplate.exchange(
          audience + "/auth/revoke",
          HttpMethod.POST,
          request,
          Void.class
      );
      log.info("Revoke successful");
    } catch (HttpClientErrorException e) {
      log.error("Revoke failed: {}", e.getMessage());
      throw new OAuthRequestException((HttpStatus) e.getStatusCode(), e.getMessage());
    }
  }

  private String generateClientSecret() {
    LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

    return Jwts.builder()
        .setHeaderParam(JwsHeader.KEY_ID, keyId)
        .setIssuer(teamId)
        .setAudience(audience)
        .setSubject(clientId)
        .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
        .setIssuedAt(new Date())
        .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
        .compact();
  }

  private PrivateKey getPrivateKey() {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

    try {
      byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey.replaceAll("\\s", ""));
      PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
      return converter.getPrivateKey(privateKeyInfo);
    } catch (Exception e) {
      throw new RuntimeException("Error converting private key from String", e);
    }
  }
}
