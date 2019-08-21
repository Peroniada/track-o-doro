package com.sperek.application;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sperek.application.controller.CategoryController;
import com.sperek.application.controller.GoalController;
import com.sperek.application.controller.SessionController;
import com.sperek.application.controller.UserController;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.plugin.json.JavalinJson;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.swagger.v3.oas.models.info.Info;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.jetbrains.annotations.NotNull;

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
        .create(config -> {
          config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
          config.registerPlugin(new RouteOverviewPlugin("routes"));
        }).start(8891);

    Gson gson = new GsonBuilder()
        .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeJsonDeserializer())
        .registerTypeAdapter(LocalDate.class, new LocalDateJsonDeserializer())
        .create();
    JavalinJson.setFromJsonMapper(gson::fromJson);
    JavalinJson.setToJsonMapper(gson::toJson);
    app.routes(sessionRoutes());
    app.routes(categoryRoutes());

    app.get("/dailyGoalFinished", goalController.dailyPomodoroGoalFinished);
  }

  @NotNull
  private EndpointGroup sessionRoutes() {
    return () -> {
      path("sessions", () -> {
        get(sessionController.getSessions);
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
    };
  }

  private EndpointGroup categoryRoutes() {
    return () -> {
      path("categories", () -> {
        get(categoryController.categoriesCreatedByUser);
        post(categoryController.createCategory);
        path(":categoryName", () -> {
          get(categoryController.getCategory);
          path("goals", () -> {
            path("daily", () -> {
              get(":onDate", categoryController.dailyGoalFinished);
            });
            path("weekly", () -> {
              get(":weekNumber", categoryController.weeklyGoalFinished);
            });
          });
        });
      });
    };
  }

  private OpenApiOptions getOpenApiOptions() {
    Info applicationInfo = new Info().version("0.1").description("Pomodoro-Tracker");
    return new OpenApiOptions(applicationInfo).path("/swagger-docs");
  }

}
