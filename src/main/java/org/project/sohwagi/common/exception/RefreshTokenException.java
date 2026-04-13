package org.project.sohwagi.common.exception;

import lombok.Getter;

@Getter
public class RefreshTokenException extends RuntimeException{

  public RefreshTokenException(String message) {
    super(message);
  }

}
