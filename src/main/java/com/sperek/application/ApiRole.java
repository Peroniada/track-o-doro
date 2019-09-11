package com.sperek.application;

import com.sperek.trackodoro.user.UserRole;
import io.javalin.core.security.Role;

enum ApiRole implements Role {
  ANYONE, USER, ADMIN
}
