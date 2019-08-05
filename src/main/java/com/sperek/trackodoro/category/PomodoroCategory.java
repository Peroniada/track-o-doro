package com.sperek.trackodoro.category;

import com.sperek.trackodoro.goal.Goal;
import com.sperek.trackodoro.goal.WeeklyGoal;
import java.util.UUID;

public class PomodoroCategory {
  private String categoryName;
  private Goal dailyGoal;
  private Goal weeklyGoal;
  private UUID ownerId;


  public PomodoroCategory(String categoryName, Goal dailyGoal, Goal weeklyGoal, UUID ownerId) {
    this.categoryName = categoryName;
    this.dailyGoal = dailyGoal;
    this.weeklyGoal = weeklyGoal;
    this.ownerId = ownerId;
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

  public void editWeeklyGoal(WeeklyGoal weeklyGoal) {
    this.weeklyGoal = weeklyGoal;
  }

  public UUID getOwnerId() {
    return this.ownerId;
  }
}
