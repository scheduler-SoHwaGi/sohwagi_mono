package org.project.sohwagi.infra.apple;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PrivateKey;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
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
import org.springframework.web.client.HttpStatusCodeException;
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

  private JwkProvider jwkProvider;

  @PostConstruct
  private void init() {
    try {
      jwkProvider = new JwkProviderBuilder(new URL(audience + "/auth/keys"))
          .cached(10, 24, TimeUnit.HOURS)
          .rateLimited(10, 1, TimeUnit.MINUTES)
          .build();
    } catch (MalformedURLException e) {
      throw new IllegalStateException("Invalid apple.signin.audience: " + audience, e);
    }
  }

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

      DecodedJWT decodedJWT = verifyIdToken(
          Objects.requireNonNull(response.getBody()).idToken());

      return new AppleOAuthInfoRes(decodedJWT.getClaim("sub").asString(),
          decodedJWT.getClaim("email").asString(), response.getBody().refreshToken());

    } catch (HttpStatusCodeException e) {
      log.error("Apple token request failed: {}", e.getMessage());
      throw new OAuthRequestException((HttpStatus) e.getStatusCode(),
          e.getMessage());
    }
  }

  private DecodedJWT verifyIdToken(String idToken) {
    try {
      String tokenKeyId = JWT.decode(idToken).getKeyId();
      Jwk jwk = jwkProvider.get(tokenKeyId);
      Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

      return JWT.require(algorithm)
          .withIssuer(audience)
          .withAudience(clientId)
          .build()
          .verify(idToken);
    } catch (JwkException | JWTVerificationException e) {
      log.error("Apple id_token verification failed: {}", e.getMessage());
      throw new OAuthRequestException(HttpStatus.UNAUTHORIZED,
          "Invalid apple id_token: " + e.getMessage());
    }
  }

  public void appleRevoke(String appleRefreshToken) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<String> request = new HttpEntity<>(
        "client_id=" + clientId +
            "&client_secret=" + generateClientSecret() +
            "&token=" + appleRefreshToken +
            "&token_type_hint=refresh_token",
        headers
    );

    try {
      restTemplate.exchange(
          audience + "/auth/revoke",
          HttpMethod.POST,
          request,
          Void.class
      );
      log.info("Revoke successful");
    } catch (HttpStatusCodeException e) {
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
