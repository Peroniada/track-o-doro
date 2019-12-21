package com.sperek.pomodorotracker.application.ports;

import com.sperek.pomodorotracker.domain.model.PomodoroCategory;
import com.sperek.pomodorotracker.domain.model.DailyGoal;
import com.sperek.pomodorotracker.domain.model.WeeklyGoal;
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
