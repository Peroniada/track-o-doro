package com.sperek.application;

import com.sperek.application.controller.CategoryController;
import com.sperek.application.controller.GoalController;
import com.sperek.application.controller.SessionController;
import com.sperek.application.controller.UserController;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.plugin.json.ToJsonMapper;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.ReDocOptions;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

public class JavalinConfig implements Runnable {

  private SessionController sessionController;
  private CategoryController categoryController;
  private GoalController goalController;
  private UserController userController;

  JavalinConfig(SessionController sessionController,
      CategoryController categoryController,
      GoalController goalController, UserController userController) {
    this.sessionController = sessionController;
    this.categoryController = categoryController;
    this.goalController = goalController;
    this.userController = userController;
  }

  @Override
  public void run() {
    Javalin app = Javalin
        .create(config -> {
          config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
          config.registerPlugin(new RouteOverviewPlugin("routes"));
        }).start(8891);

    app.routes(sessionController.sessionRoutes());
    app.routes(categoryController.categoryRoutes());

    app.get("/dailyGoalFinished", goalController.dailyPomodoroGoalFinished);

  }

  private OpenApiOptions getOpenApiOptions() {
    Info applicationInfo = new Info().version("0.1").description("Pomodoro-Tracker");
    return new OpenApiOptions(applicationInfo)
        .path("/swagger-docs")
        .swagger(new SwaggerOptions("/swagger").title("My Swagger Documentation"));
  }
}
