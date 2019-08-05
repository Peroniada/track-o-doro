package com.sperek.trackodoro.tracker.category;

import com.sperek.trackodoro.category.PomodoroCategory;
import com.sperek.trackodoro.goal.DailyGoal;
import com.sperek.trackodoro.goal.WeeklyGoal;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class PomodoroCategoryEngineImpl implements PomodoroCategoryEngine {

  private PomodoroCategoryRepository categoryRepository;

  public PomodoroCategoryEngineImpl(
      PomodoroCategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public Integer dailyGoalForCategory(String categoryName) {
    return categoryByName(categoryName).sessionsToCompleteDailyGoal();
  }

  @Override
  public Integer weeklyGoalForCategory(String categoryName) {
    return categoryByName(categoryName).sessionsToCompleteWeeklyGoal();
  }

  @Override
  public void editDailyGoalForCategory(String categoryName, DailyGoal dailyGoal) {
    final PomodoroCategory pomodoroCategory = categoryByName(categoryName);
    pomodoroCategory.editDailyGoal(dailyGoal);
    categoryRepository.save(pomodoroCategory);
  }

  @Override
  public void editWeeklyGoalForCategory(String categoryName, WeeklyGoal weeklyGoal) {
    final PomodoroCategory pomodoroCategory = categoryRepository.findByName(categoryName);
    pomodoroCategory.editWeeklyGoal(weeklyGoal);
  }

  @Override
  public void createCategory(PomodoroCategory pomodoroCategory) {
    categoryRepository.save(pomodoroCategory);
  }

  @Override
  public Collection<PomodoroCategory> categoriesCreatedByUser(UUID ownerId) {
    return categoryRepository.findAll().stream().filter(category -> category.getOwnerId().equals(ownerId)).collect(
        Collectors.toSet());
  }

  private PomodoroCategory categoryByName(String categoryName) {
    return categoryRepository.findByName(categoryName);
  }
}