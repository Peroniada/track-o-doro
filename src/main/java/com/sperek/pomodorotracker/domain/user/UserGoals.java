package com.sperek.pomodorotracker.domain.user;

import com.sperek.pomodorotracker.domain.model.DailyGoal;
import com.sperek.pomodorotracker.domain.model.WeeklyGoal;
import java.util.UUID;

public class UserGoals {
  private UUID userGoalsId;
  private DailyGoal dailyGoal;
  private WeeklyGoal weeklyGoal;

  public UserGoals(UUID userGoalsId, DailyGoal dailyGoal, WeeklyGoal weeklyGoal) {
    this.dailyGoal = dailyGoal;
    this.weeklyGoal = weeklyGoal;
    this.userGoalsId = userGoalsId;
  }

  public Integer getDailyGoal() {
    return dailyGoal.getNumberOfSessionsToFulfill();
  }

  public void setDailyGoal(DailyGoal dailyGoal) {
    this.dailyGoal = dailyGoal;
  }

  public Integer getWeeklyGoal() {
    return weeklyGoal.getNumberOfSessionsToFulfill();
  }

  public void setWeeklyGoal(WeeklyGoal weeklyGoal) {
    this.weeklyGoal = weeklyGoal;
  }

  public UUID getUserGoalsId() {
    return userGoalsId;
  }
}
