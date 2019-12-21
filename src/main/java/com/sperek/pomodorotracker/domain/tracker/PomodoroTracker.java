package com.sperek.pomodorotracker.domain.tracker;

import com.sperek.pomodorotracker.domain.sessionFilter.composite.spec.Specification;
import com.sperek.pomodorotracker.domain.model.PomodoroCategory;
import com.sperek.pomodorotracker.domain.model.DailyGoal;
import com.sperek.pomodorotracker.domain.model.Goal;
import com.sperek.pomodorotracker.domain.model.WeeklyGoal;
import com.sperek.pomodorotracker.domain.sessionFilter.CategorySpecification;
import com.sperek.pomodorotracker.domain.sessionFilter.DateSpecification;
import com.sperek.pomodorotracker.domain.sessionFilter.OwnerSpecification;
import com.sperek.pomodorotracker.domain.sessionFilter.WeekOfYearSpecification;
import com.sperek.pomodorotracker.application.ports.PomodoroCategoryEngine;
import com.sperek.pomodorotracker.domain.tracker.session.PomodoroSession;
import com.sperek.pomodorotracker.application.ports.PomodoroSessionEngine;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PomodoroTracker {

  private PomodoroSessionEngine<UUID> sessionEngine;
  private PomodoroCategoryEngine categoryEngine;
  private WeeklyGoal weeklyGoal;
  private DailyGoal dailyGoal;

  public PomodoroTracker(PomodoroSessionEngine<UUID> sessionEngine,
      PomodoroCategoryEngine categoryEngine) {
    this.sessionEngine = sessionEngine;
    this.categoryEngine = categoryEngine;
  }

  public void saveSession(PomodoroSession session) {
    sessionEngine.saveOne(session);
  }

  public void saveSessions(Collection<PomodoroSession> sessions) {
    sessionEngine.saveAll(sessions);
  }

  public PomodoroSession getSession(UUID sessionId) {
    return sessionEngine.getSession(sessionId);
  }

  public Collection<PomodoroSession> findSatisfyingSessions(
      Specification<PomodoroSession> sessionSpecification) {
    return sessionEngine.findSatisfyingSessions(sessionSpecification);
  }

  public Integer countSatisfyingSessions(Specification<PomodoroSession> sessionSpecification) {
    return sessionEngine.findSatisfyingSessions(sessionSpecification).size();
  }

  public Collection<PomodoroSession> allUserSessions(UUID ownerId) {
    return sessionEngine.allUserSessions(ownerId);
  }

  public Boolean dailyPomodoroGoalFinished(LocalDate date) {
    final DateSpecification sessionSpecification = new DateSpecification(date);
    return countSatisfyingSessions(sessionSpecification) >= (userDailyGoalSessionsNumber());
  }

  public Boolean dailyPomodoroGoalForCategoryFinished(UUID ownerId, String category, LocalDate date) {
    Integer sessionsToCompleteDailyGoal = categoryEngine.dailyGoalForCategory(ownerId, category);
    Specification<PomodoroSession> specification = new OwnerSpecification(ownerId).and(new CategorySpecification(category).and(new DateSpecification(date)));
    return countSatisfyingSessions(specification)>= sessionsToCompleteDailyGoal;
  }

  public Boolean weeklyPomodoroGoalForCategoryFinished(UUID ownerId, String category, LocalDate date) {
    Integer sessionsToCompleteDailyGoal = categoryEngine.weeklyGoalForCategory(ownerId, category);
    Specification<PomodoroSession> specification = new OwnerSpecification(ownerId).and(new CategorySpecification(category).and(new WeekOfYearSpecification(weekNumberOfDate(date))));
    return countSatisfyingSessions(specification)>= sessionsToCompleteDailyGoal;
  }

  public void editWeeklyGoal(WeeklyGoal weeklyGoal) {
    this.weeklyGoal = weeklyGoal;
  }

  public Boolean weeklyGoalFinishedForDate(LocalDate dateTime) {
    final int week = weekNumberOfDate(dateTime);
    final Integer weeklyGoal = this.weeklyGoal.getNumberOfSessionsToFulfill();
    return weeklyGoal <= countSessionsForWeek(week);
  }

  public void editDailyGoal(DailyGoal dailyGoal) {
    this.dailyGoal = dailyGoal;
  }

  public Integer userDailyGoalSessionsNumber() {
    return dailyGoalOfUser().getNumberOfSessionsToFulfill();
  }

  public Map<String, Long> sessionsSummaryForUser(UUID ownerId) {
    return allUserSessions(ownerId).stream()
        .collect(
            Collectors.groupingBy(PomodoroSession::sessionCategoryName, Collectors.counting()));
  }

  public Integer weeklyGoalForCategory(UUID ownerId, String category) {
    return categoryEngine.weeklyGoalForCategory(ownerId, category);
  }

  public void editWeeklyGoalForCategory(UUID ownerId, String categoryName, WeeklyGoal weeklyGoal) {
    categoryEngine.editWeeklyGoalForCategory(ownerId, categoryName, weeklyGoal);
  }

  public void createCategory(PomodoroCategory pomodoroCategory) {
    categoryEngine.createCategory(pomodoroCategory);
  }

  public PomodoroCategory getCategory(UUID ownerId, String categoryName) {
    return categoryEngine.categoryByName(ownerId, categoryName);
  }

  public Collection<PomodoroCategory> categoriesCreatedByUser(UUID ownerId) {
    return categoryEngine.categoriesCreatedByUser(ownerId);
  }

  private Goal dailyGoalOfUser() {
    return dailyGoal;
  }

  private int weekNumberOfDate(LocalDate dateTime) {
    return dateTime.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
  }

  private Integer countSessionsForWeek(Integer week) {
    return sessionEngine.findSatisfyingSessions(new WeekOfYearSpecification(week)).size();
  }
}
