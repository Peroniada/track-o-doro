package com.sperek.trackodoro.user.exceptions;

public class LoginException extends RuntimeException {
  private static final String message = "Invalid mail or password";

  public LoginException() {
    super(message);
  }
}
