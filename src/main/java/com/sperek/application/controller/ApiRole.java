package com.sperek.application.controller;

import io.javalin.core.security.Role;

public enum ApiRole implements Role {
  ANYONE, USER, ADMIN
}
