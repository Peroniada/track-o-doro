package com.sperek.pomodorotracker.infrastructure.persistence;

public class DBConnectionException extends RuntimeException {

  public DBConnectionException(String message) {
    super(message);
  }
}
