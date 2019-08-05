package com.sperek.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sperek.application.controller.CategoryController;
import com.sperek.application.controller.GoalController;
import com.sperek.application.controller.SessionController;
import com.sperek.application.controller.UserController;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJson;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.swagger.v3.oas.models.info.Info;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ApplicationRunner implements Runnable {

  private SessionController sessionController;
  private CategoryController categoryController;
  private GoalController goalController;
  private UserController userController;

  public ApplicationRunner(SessionController sessionController,
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
        .create(config -> config.registerPlugin(new OpenApiPlugin(getOpenApiOptions())))
        .start(8891);
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeJsonDeserializer())
        .registerTypeAdapter(LocalDate.class, new LocalDateJsonDeserializer())
        .create();
    JavalinJson.setFromJsonMapper(gson::fromJson);
    JavalinJson.setToJsonMapper(gson::toJson);

    app.routes(() -> {
      path("sessions", () -> {
        get(sessionController.allUserSessions);
        post(sessionController.saveSession);
        path("count", () -> {
          get(sessionController.countSessions);
        });
        path("summary", () -> {
          get(sessionController.sessionsSummaryForUser);
        });
        path(":id", () -> {
          get(sessionController.getSession);
        });
      });
    });

    app.get("/dailyGoalFinished", goalController.dailyPomodoroGoalFinished);
    app.get("/dailyGoalForCategoryFinished", goalController.dailyPomodoroGoalForCategoryFinished);

  }

  private OpenApiOptions getOpenApiOptions() {
    Info applicationInfo = new Info().version("0.1").description("Pomodoro-Tracker");
    return new OpenApiOptions(applicationInfo).path("/swagger-docs");
  }

}
