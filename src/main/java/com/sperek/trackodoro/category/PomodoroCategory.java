package com.sperek.trackodoro.category;

import com.sperek.trackodoro.goal.Goal;

public class PomodoroCategory {
  private String categoryName;
  private Goal dailyGoal;
  private Goal weeklyGoal;

  public PomodoroCategory(String categoryName, Goal dailyGoal, Goal weeklyGoal) {
    this.categoryName = categoryName;
    this.dailyGoal = dailyGoal;
    this.weeklyGoal = weeklyGoal;
  }

  public Integer sessionsToCompleteWeeklyGoal() {
    return weeklyGoal.getNumberOfSessionsToFulfill();
  }
  public Integer sessionsToCompleteDailyGoal() {
    return dailyGoal.getNumberOfSessionsToFulfill();
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void editDailyGoal(Goal newDailyGoal) {
    this.dailyGoal = newDailyGoal;
  }
}
