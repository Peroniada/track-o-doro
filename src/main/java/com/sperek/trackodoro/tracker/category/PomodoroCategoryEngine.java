package com.sperek.trackodoro.tracker.category;

import com.sperek.trackodoro.category.PomodoroCategory;
import com.sperek.trackodoro.goal.DailyGoal;
import com.sperek.trackodoro.goal.WeeklyGoal;
import java.util.Collection;
import java.util.UUID;

public interface PomodoroCategoryEngine {

  Integer dailyGoalForCategory(UUID ownerId, String categoryName);

  Integer weeklyGoalForCategory(UUID ownerId, String categoryName);

  void editDailyGoalForCategory(UUID ownerId, String categoryName, DailyGoal dailyGoal);

  void editWeeklyGoalForCategory(UUID ownerId, String categoryName, WeeklyGoal weeklyGoal);

  void createCategory(PomodoroCategory pomodoroCategory);

  Collection<PomodoroCategory> categoriesCreatedByUser(UUID ownerId);

  PomodoroCategory categoryByName(UUID ownerId, String categoryName);

}
