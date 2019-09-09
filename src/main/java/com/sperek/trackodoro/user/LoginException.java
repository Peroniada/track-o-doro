package com.sperek.trackodoro.user;

class LoginException extends RuntimeException{
  private static final String message = "Invalid mail or password";

  LoginException() {
    super(message);
  }
}
