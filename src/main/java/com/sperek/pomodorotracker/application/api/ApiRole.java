package com.sperek.pomodorotracker.application.api;

import io.javalin.core.security.Role;

public enum ApiRole implements Role {
  ANYONE, USER, ADMIN
}
