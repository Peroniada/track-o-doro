package com.sperek.pomodorotracker.domain.user;

public class UserNotFoundException extends RuntimeException {

  private static final String MESSAGE = "userNotFoundxD";

  public UserNotFoundException() {
    super(MESSAGE);
  }
}
