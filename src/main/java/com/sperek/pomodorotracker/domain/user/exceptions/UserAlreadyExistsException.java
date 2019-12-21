package com.sperek.pomodorotracker.domain.user.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

  private final static String message = "User with registered email already exists.";

  public UserAlreadyExistsException() {
    super(message);
  }
}
