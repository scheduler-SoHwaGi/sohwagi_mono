package org.project.sohwagi.common.exception;

import lombok.Getter;

@Getter
public class AccessTokenException extends RuntimeException{

  private final String newAccessToken;

  public AccessTokenException(String message, String newAccessToken){
    super(message);
    this.newAccessToken = newAccessToken;
  }



}
