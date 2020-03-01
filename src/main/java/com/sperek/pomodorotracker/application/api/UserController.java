package com.sperek.pomodorotracker.application.api;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.put;
import static io.javalin.plugin.openapi.dsl.OpenApiBuilder.document;
import static io.javalin.plugin.openapi.dsl.OpenApiBuilder.documented;

import com.sperek.pomodorotracker.application.api.requests.ChangePasswordRequest;
import com.sperek.pomodorotracker.application.security.JWTTokenizer;
import com.sperek.pomodorotracker.domain.tracker.dto.UserDTO;
import com.sperek.pomodorotracker.domain.user.PasswordEncryptor;
import com.sperek.pomodorotracker.domain.user.User;
import com.sperek.pomodorotracker.domain.user.UserSystem;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.core.security.SecurityUtil;
import io.javalin.http.Handler;
import io.javalin.plugin.openapi.dsl.OpenApiDocumentation;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserController {

  private UserSystem userSystem;
  private JWTTokenizer tokenizer;
  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  public UserController(UserSystem userSystem, JWTTokenizer tokenizer) {
    this.userSystem = userSystem;
    this.tokenizer = tokenizer;
  }

  public EndpointGroup apiRoutes() {
    return userRoutes;
  }

  private OpenApiDocumentation createAccountDoc = document()
      .body(UserDTO.class)
      .result("201")
      .result("409");

  private Handler createAccount = ctx -> {
    UserDTO newUser = ctx.bodyAsClass(UserDTO.class);
    userSystem
        .createAccount(newUser);
    ctx.status(201);
  };

  private OpenApiDocumentation loginDoc = document()
      .body(UserDTO.class)
      .result("200")
      .result("401");

  private Handler login = ctx -> {
    UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
    User user = userSystem.login(userDTO.getUserMail(), userDTO.getPassword());
    final String token = tokenizer
        .generate(user.getId().toString(), user.getUserRole().toString());
    ctx.header("token", token);
    ctx.status(200);
  };

  private OpenApiDocumentation changePasswordDoc = document()
      .body(UserDTO.class)
      .result("200")
      .result("401");

  private Handler changePassword = ctx -> {
    final UUID userId = UUID
        .fromString(tokenizer.parser(ctx.header("token")).getBody().getSubject());
    final ChangePasswordRequest request = ctx.bodyAsClass(ChangePasswordRequest.class);
    userSystem.changePassword(userId, request.getOldPassword(), request.getNewPassword());
    ctx.status(201);
  };

  private EndpointGroup userRoutes = () -> path(
      "users", () -> {
        post(documented(createAccountDoc,createAccount));
        post("login", documented(loginDoc, login));
        put(documented(changePasswordDoc, changePassword), SecurityUtil.roles(ApiRole.USER));
      }
  );
}
