package com.sperek.pomodorotracker.application;

import io.javalin.core.security.Role;

enum ApiRole implements Role {
  ANYONE, USER, ADMIN
}
