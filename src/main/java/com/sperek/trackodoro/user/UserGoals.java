package com.sperek.trackodoro.user;

import com.sperek.trackodoro.goal.DailyGoal;
import com.sperek.trackodoro.goal.WeeklyGoal;

public class UserGoals {
  private DailyGoal dailyGoal;
  private WeeklyGoal weeklyGoal;

  public UserGoals(DailyGoal dailyGoal, WeeklyGoal weeklyGoal) {
    this.dailyGoal = dailyGoal;
    this.weeklyGoal = weeklyGoal;
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
}
