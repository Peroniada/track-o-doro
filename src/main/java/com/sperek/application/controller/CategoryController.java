package com.sperek.application.controller;

import com.sperek.trackodoro.category.PomodoroCategory;
import com.sperek.trackodoro.tracker.PomodoroTracker;
import io.javalin.http.Handler;
import java.util.Optional;
import java.util.UUID;

public class CategoryController {

  private PomodoroTracker tracker;

  public CategoryController(PomodoroTracker tracker) {
    this.tracker = tracker;
  }

  public Handler createCategory = ctx -> {
    tracker.createCategory(ctx.bodyAsClass(PomodoroCategory.class));
    ctx.status(201);
  };

  public Handler categoriesCreatedByUser = ctx -> {
    tracker.categoriesCreatedByUser(UUID.fromString(Optional.ofNullable(ctx.header("userId")).orElseThrow(() -> new RuntimeException("No userId provided"))));
    ctx.status(200);
  };
}
