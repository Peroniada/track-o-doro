package com.sperek.application.api.category;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.core.security.SecurityUtil.roles;
import static io.javalin.plugin.openapi.dsl.OpenApiBuilder.document;
import static io.javalin.plugin.openapi.dsl.OpenApiBuilder.documented;

import com.sperek.application.api.ApiRole;
import com.sperek.application.security.JWTTokenizer;
import com.sperek.trackodoro.PomodoroCategoryMapper;
import com.sperek.trackodoro.category.PomodoroCategory;
import com.sperek.trackodoro.tracker.PomodoroTracker;
import com.sperek.trackodoro.tracker.dto.PomodoroCategoryDTO;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.plugin.openapi.dsl.OpenApiDocumentation;
import java.time.LocalDate;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class CategoryController {

  private static final String CURRENT_USER_HEADER = "Token";
  private PomodoroTracker tracker;
  private JWTTokenizer tokenizer;


  public CategoryController(PomodoroTracker tracker, JWTTokenizer tokenizer) {
    this.tracker = tracker;
    this.tokenizer = tokenizer;
  }

  public EndpointGroup apiRoutes() {
    return categoryRoutes;
  }
  private OpenApiDocumentation createCategoryDoc = document()
      .header(CURRENT_USER_HEADER, String.class)
      .body(PomodoroCategoryDTO.class)
      .result("201")
      .result("403")
      .result("401");

  private Handler createCategory = ctx -> {
    final PomodoroCategory pomodoroCategory = PomodoroCategoryMapper.fromDto
        .apply(ctx.bodyAsClass(PomodoroCategoryDTO.class), ownerId(ctx));
    tracker.createCategory(pomodoroCategory);
    ctx.status(201);
  };

  private OpenApiDocumentation categoriesByUserDoc = document()
      .header(CURRENT_USER_HEADER, String.class)
      .jsonArray("200", PomodoroCategoryDTO.class)
      .result("401")
      .result("403")
      .result("404");

  private Handler categoriesCreatedByUser = ctx -> {
    tracker.categoriesCreatedByUser(ownerId(ctx));
    ctx.status(200);
  };

  private OpenApiDocumentation getCategoryDoc = document()
      .header(CURRENT_USER_HEADER, String.class)
      .queryParam("categoryName", String.class)
      .json("200", PomodoroCategoryDTO.class)
      .result("401")
      .result("403")
      .result("404");

  private Handler getCategory = ctx -> ctx.json(PomodoroCategoryMapper.toDto
      .apply(tracker.getCategory(ownerId(ctx), ctx.pathParam("categoryName"))));

  private OpenApiDocumentation dailyGoalFinishedDoc = document()
      .header(CURRENT_USER_HEADER, String.class)
      .queryParam("categoryName", String.class)
      .queryParam("date", String.class)
      .json("200", Boolean.class)
      .result("401")
      .result("403")
      .result("404");

  private Handler dailyGoalFinished = ctx -> {
    final UUID ownerId = ownerId(ctx);
    final String categoryName = ctx.pathParam("categoryName");
    final LocalDate date = LocalDate.parse(ctx.pathParam("date"));
    ctx.json(tracker.dailyPomodoroGoalForCategoryFinished(ownerId, categoryName, date));
    ctx.json(200);
  };

  private OpenApiDocumentation weeklyGoalFinishedDoc = document()
      .header(CURRENT_USER_HEADER, String.class)
      .queryParam("categoryName", String.class)
      .queryParam("date", String.class)
      .json("200", Boolean.class)
      .result("401")
      .result("403")
      .result("404");

  private Handler weeklyGoalFinished = ctx -> {
    final UUID ownerId = ownerId(ctx);
    final String categoryName = ctx.pathParam("categoryName");
    final LocalDate date = LocalDate.parse(ctx.pathParam("date"));
    ctx.json(tracker.weeklyPomodoroGoalForCategoryFinished(ownerId, categoryName, date));
    ctx.json(200);
  };

  @NotNull
  private UUID ownerId(Context ctx) {
    return UUID.fromString(tokenizer.parser(ctx.header(CURRENT_USER_HEADER)).getSignature());
  }

  private EndpointGroup categoryRoutes = () -> path("categories", () -> {
    get(documented(categoriesByUserDoc, categoriesCreatedByUser), roles(ApiRole.USER));
    post(documented(createCategoryDoc, createCategory), roles(ApiRole.USER));
    path(":categoryName", () -> {
      get(documented(getCategoryDoc, getCategory), roles(ApiRole.USER));
      path("goals", () -> {
        path("daily", () ->
            get(":onDate", documented(dailyGoalFinishedDoc, dailyGoalFinished), roles(ApiRole.USER)));
        path("weekly", () ->
            get(":weekNumber", documented(weeklyGoalFinishedDoc, weeklyGoalFinished), roles(ApiRole.USER)));
      });
    });
  });
}
