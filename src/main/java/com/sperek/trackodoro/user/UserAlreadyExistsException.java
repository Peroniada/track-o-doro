package com.sperek.trackodoro.user;

public class UserAlreadyExistsException extends RuntimeException {

  private final static String message = "User with registered email already exists.";

  UserAlreadyExistsException() {
    super(message);
  }
}
