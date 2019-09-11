package com.sperek.trackodoro.user.exceptions;

public class PasswordChangeException extends RuntimeException {
  private final static String message = "Password change failed.";

  public PasswordChangeException() {
    super(message);
  }
}
