package com.sperek.pomodorotracker.application;

import com.sperek.pomodorotracker.application.api.*;
import com.sperek.pomodorotracker.application.api.ApiRole;
import com.sperek.pomodorotracker.application.security.JWTTokenizer;
import io.javalin.Javalin;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.Role;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;
import java.util.Optional;

public class JavalinConfig implements Runnable {

  private PomodoroApi api;
  private JWTTokenizer tokenizer;


  public JavalinConfig(PomodoroApi pomodoroApi, JWTTokenizer tokenizer) {
    this.api = pomodoroApi;
    this.tokenizer = tokenizer;
  }

  private AccessManager accessManager = (handler, ctx, permittedRoles) ->  {
      Role userRole = determineUserRole(ctx);
      if (permittedRoles.contains(userRole) || permittedRoles.isEmpty()) {
        handler.handle(ctx);
      } else {
        ctx.status(401).result("Unauthorized");
      }
  };

  private Role determineUserRole(Context ctx) {
    String role = "ANYONE";
    String token = Optional.ofNullable(ctx.header("Token")).orElse("");
    if(!token.isEmpty()) {
      role = tokenizer.parser(token).getBody().get("role").toString();
    }
    return ApiRole.valueOf(role.toUpperCase());
  }

  @Override
  public void run() {
    Javalin app = Javalin
        .create(config -> {
          config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
          config.registerPlugin(new RouteOverviewPlugin("routes"));
          config.accessManager(accessManager);
        }).start(8891);

    app.routes(api.getPomodoroController().apiRoutes());
    app.routes(api.getCategoryController().apiRoutes());
    app.routes(api.getUserController().apiRoutes());

    app.get("/dailyGoalFinished", api.getGoalController().dailyPomodoroGoalFinished);

  }

  private OpenApiOptions getOpenApiOptions() {
    Info applicationInfo = new Info().version("0.1").description("Pomodoro-Tracker");
    return new OpenApiOptions(applicationInfo)
        .path("/swagger-docs")
        .swagger(new SwaggerOptions("/swagger").title("My Swagger Documentation"));
  }
}
