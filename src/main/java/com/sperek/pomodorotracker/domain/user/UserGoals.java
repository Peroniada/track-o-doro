package com.sperek.pomodorotracker.domain.user;

import com.sperek.pomodorotracker.domain.model.DailyGoal;
import com.sperek.pomodorotracker.domain.model.WeeklyGoal;
import java.util.UUID;

public class UserGoals {
  private UUID userGoalsId;
  private DailyGoal dailyGoal;
  private WeeklyGoal weeklyGoal;

  public UserGoals(DailyGoal dailyGoal, WeeklyGoal weeklyGoal, UUID userGoalsId) {
    this.dailyGoal = dailyGoal;
    this.weeklyGoal = weeklyGoal;
    this.userGoalsId = userGoalsId;
  }

  public DailyGoal getDailyGoal() {
    return dailyGoal;
  }

  public void setDailyGoal(DailyGoal dailyGoal) {
    this.dailyGoal = dailyGoal;
  }

  public WeeklyGoal getWeeklyGoal() {
    return weeklyGoal;
  }

  public void setWeeklyGoal(WeeklyGoal weeklyGoal) {
    this.weeklyGoal = weeklyGoal;
  }

  public UUID getUserGoalsId() {
    return userGoalsId;
  }
}
