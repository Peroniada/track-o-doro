package com.sperek.pomodorotracker.application.api;

import com.sperek.pomodorotracker.domain.model.DailyGoal;
import com.sperek.pomodorotracker.domain.model.WeeklyGoal;
import com.sperek.pomodorotracker.domain.tracker.PomodoroTracker;
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

  private LocalDate occurrenceFrom(String dateString) {
    return LocalDate.parse(Optional.ofNullable(dateString)
        .orElseThrow(() -> new RuntimeException("No date provided")));
  }
}
