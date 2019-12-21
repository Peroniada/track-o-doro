package com.sperek.pomodorotracker.domain.tracker.category;

import com.sperek.pomodorotracker.application.ports.PomodoroCategoryEngine;
import com.sperek.pomodorotracker.application.ports.secondary.PomodoroCategoryRepository;
import com.sperek.pomodorotracker.domain.model.PomodoroCategory;
import com.sperek.pomodorotracker.domain.model.DailyGoal;
import com.sperek.pomodorotracker.domain.model.WeeklyGoal;
import java.util.Collection;
import java.util.UUID;

public class PomodoroCategoryEngineImpl implements PomodoroCategoryEngine {

  private PomodoroCategoryRepository categoryRepository;

  public PomodoroCategoryEngineImpl(
      PomodoroCategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public Integer dailyGoalForCategory(UUID ownerId, String categoryName) {
    return categoryByName(ownerId, categoryName).sessionsToCompleteDailyGoal();
  }

  @Override
  public Integer weeklyGoalForCategory(UUID ownerId, String categoryName) {
    return categoryByName(ownerId, categoryName).sessionsToCompleteWeeklyGoal();
  }

  @Override
  public void editDailyGoalForCategory(UUID ownerId, String categoryName, DailyGoal dailyGoal) {
    final PomodoroCategory pomodoroCategory = categoryByName(ownerId, categoryName);
    pomodoroCategory.editDailyGoal(dailyGoal);
    categoryRepository.save(pomodoroCategory);
  }

  @Override
  public void editWeeklyGoalForCategory(UUID ownerId, String categoryName, WeeklyGoal weeklyGoal) {
    final PomodoroCategory pomodoroCategory = categoryRepository.findByName(ownerId, categoryName);
    pomodoroCategory.editWeeklyGoal(weeklyGoal);
  }

  @Override
  public void createCategory(PomodoroCategory pomodoroCategory) {
    categoryRepository.save(pomodoroCategory);
  }

  @Override
  public Collection<PomodoroCategory> categoriesCreatedByUser(UUID ownerId) {
    return categoryRepository.findAll(ownerId);
  }

  @Override
  public PomodoroCategory categoryByName(UUID ownerId, String categoryName) {
    return categoryRepository.findByName(ownerId, categoryName);
  }
}