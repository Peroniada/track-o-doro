package com.sperek.application.controller;

import com.sperek.trackodoro.goal.DailyGoal;
import com.sperek.trackodoro.goal.WeeklyGoal;
import com.sperek.trackodoro.tracker.PomodoroTracker;
import io.javalin.http.Handler;
import java.time.LocalDate;
import java.util.Optional;

public class GoalController {

  private PomodoroTracker tracker;

  public GoalController(PomodoroTracker tracker) {
    this.tracker = tracker;
  }

  public Handler dailyPomodoroGoalFinished = ctx -> {
    LocalDate occurrence = occurrenceFrom(ctx.queryParam("date"));
    ctx.json(tracker.dailyPomodoroGoalFinished(occurrence));
    ctx.status(200);
  };

  public Handler dailyPomodoroGoalForCategoryFinished = ctx -> {
    final String categoryName = ctx.queryParam("categoryName");
    LocalDate occurrence = occurrenceFrom(ctx.queryParam("date"));
    ctx.json(tracker.dailyPomodoroGoalForCategoryFinished(categoryName, occurrence));
    ctx.status(200);
  };

  Handler editWeeklyGoal = ctx -> {
    WeeklyGoal weeklyGoal = ctx.bodyAsClass(WeeklyGoal.class);
    tracker.editWeeklyGoal(weeklyGoal);
  };

  Handler weeklyGoalFinishedForDate = ctx -> {
    final LocalDate date = ctx.queryParam("date", LocalDate.class).get();
    ctx.json((tracker.weeklyGoalFinishedForDate(date)));
    ctx.status(200);
  };

  Handler editDailyGoal = ctx -> {
    DailyGoal dailyGoal = ctx.bodyAsClass(DailyGoal.class);
    tracker.editDailyGoal(dailyGoal);
  };

  Handler userDailyGoalSessionsNumber = ctx -> {
    ctx.status(200);

  };

  public Handler weeklyGoalForCategory = ctx ->  {

  };

  public Handler editWeeklyGoalForCategory(String categoryName, WeeklyGoal weeklyGoal) {
    return null;
  }

  private LocalDate occurrenceFrom(String dateString) {
    return LocalDate.parse(Optional.ofNullable(dateString)
        .orElseThrow(() -> new RuntimeException("No date provided")));
  }
}
