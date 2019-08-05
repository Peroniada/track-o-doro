package com.sperek.trackodoro.tracker.category;

import com.sperek.trackodoro.category.PomodoroCategory;
import com.sperek.trackodoro.goal.DailyGoal;
import com.sperek.trackodoro.goal.WeeklyGoal;
import java.util.Collection;
import java.util.UUID;

public interface PomodoroCategoryEngine {

  Integer dailyGoalForCategory(String categoryName);

  Integer weeklyGoalForCategory(String categoryName);

  void editDailyGoalForCategory(String categoryName, DailyGoal dailyGoal);

  void editWeeklyGoalForCategory(String categoryName, WeeklyGoal weeklyGoal);

  void createCategory(PomodoroCategory pomodoroCategory);

  Collection<PomodoroCategory> categoriesCreatedByUser(UUID ownerId);
}
