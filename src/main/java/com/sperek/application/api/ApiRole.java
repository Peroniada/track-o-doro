package com.sperek.application.api;

import io.javalin.core.security.Role;

public enum ApiRole implements Role {
  ANYONE, USER, ADMIN
}
