package com.sperek.application.controller;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

import com.sperek.trackodoro.PomodoroCategoryMapper;
import com.sperek.trackodoro.category.PomodoroCategory;
import com.sperek.trackodoro.tracker.PomodoroTracker;
import com.sperek.trackodoro.tracker.dto.PomodoroCategoryDTO;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class CategoryController {

  private PomodoroTracker tracker;

  public CategoryController(PomodoroTracker tracker) {
    this.tracker = tracker;
  }

  public EndpointGroup categoryRoutes() {
    return categoryRoutes;
  }

  private Handler createCategory = ctx -> {
    final PomodoroCategory pomodoroCategory = PomodoroCategoryMapper.fromDto
        .apply(ctx.bodyAsClass(PomodoroCategoryDTO.class), ownerId(ctx));
    tracker.createCategory(pomodoroCategory);
    ctx.status(201);
  };

  private Handler categoriesCreatedByUser = ctx -> {
    tracker.categoriesCreatedByUser(ownerId(ctx));
    ctx.status(200);
  };

  private Handler getCategory = ctx -> ctx.json(PomodoroCategoryMapper.toDto
      .apply(tracker.getCategory(ownerId(ctx), ctx.pathParam("categoryName"))));

  private Handler dailyGoalFinished = ctx -> {
    final UUID ownerId = ownerId(ctx);
    final String categoryName = ctx.pathParam("categoryName");
    final LocalDate date = LocalDate.parse(ctx.pathParam("date"));
    ctx.json(tracker.dailyPomodoroGoalForCategoryFinished(ownerId, categoryName, date));
    ctx.json(200);
  };

  private Handler weeklyGoalFinished = ctx -> {
    final UUID ownerId = ownerId(ctx);
    final String categoryName = ctx.pathParam("categoryName");
    final LocalDate date = LocalDate.parse(ctx.pathParam("date"));
    ctx.json(tracker.weeklyPomodoroGoalForCategoryFinished(ownerId, categoryName, date));
    ctx.json(200);
  };

  @NotNull
  private UUID ownerId(Context ctx) {
    return UUID.fromString(Optional.ofNullable(ctx.header("Current-User"))
        .orElseThrow(() -> new RuntimeException("No userId provided")));
  }

  private EndpointGroup categoryRoutes = () -> path("categories", () -> {
    get(categoriesCreatedByUser);
    post(createCategory);
    path(":categoryName", () -> {
      get(getCategory);
      path("goals", () -> {
        path("daily", () ->
            get(":onDate", dailyGoalFinished));
        path("weekly", () ->
            get(":weekNumber", weeklyGoalFinished));
      });
    });
  });
}
