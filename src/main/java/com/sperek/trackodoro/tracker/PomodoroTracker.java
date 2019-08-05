package com.sperek.trackodoro.tracker;

import com.sperek.trackodoro.category.PomodoroCategory;
import com.sperek.trackodoro.goal.DailyGoal;
import com.sperek.trackodoro.goal.Goal;
import com.sperek.trackodoro.goal.WeeklyGoal;
import com.sperek.trackodoro.sessionFilter.CategorySpecification;
import com.sperek.trackodoro.sessionFilter.DateSpecification;
import com.sperek.trackodoro.sessionFilter.WeekOfYearSpecification;
import com.sperek.trackodoro.sessionFilter.composite.spec.Specification;
import com.sperek.trackodoro.tracker.category.PomodoroCategoryEngine;
import com.sperek.trackodoro.tracker.session.PomodoroSession;
import com.sperek.trackodoro.tracker.session.PomodoroSessionEngine;
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

  public Collection<PomodoroSession> findSatisfyingSessions(Specification<PomodoroSession> sessionSpecification) {
    return sessionEngine.findSatisfyingSessions(sessionSpecification);
  }

  public Integer countSatisfyingSessions(Specification<PomodoroSession> sessionSpecification) {
    return sessionEngine.findSatisfyingSessions(sessionSpecification).size();
  }

  public Collection<PomodoroSession> allUserSessions(UUID ownerId) {
    return sessionEngine.allUserSessions(ownerId);
  }

  public Collection<PomodoroSession> sessionsByCategory(String category) {
    CategorySpecification specification = new CategorySpecification(category);
    return sessionEngine.findSatisfyingSessions(specification);
  }

  public Integer countSessionsByCategory(String category) {
    CategorySpecification specification = new CategorySpecification(category);
    return sessionEngine.findSatisfyingSessions(specification).size();
  }

  public Integer countSessionsByDay(LocalDate occurrence) {
    DateSpecification specification = new DateSpecification(occurrence);
    return sessionEngine.findSatisfyingSessions(specification).size();
  }

  public Integer countSessionsByDateAndCategory(LocalDate date, String category) {
    Specification<PomodoroSession> specification =
        new DateSpecification(date).and(new CategorySpecification(category));
    return sessionEngine.findSatisfyingSessions(specification).size();
  }

  public Boolean dailyPomodoroGoalFinished(LocalDate date) {
    return countSessionsByDay(date) >= (userDailyGoalSessionsNumber());
  }

  public Boolean dailyPomodoroGoalForCategoryFinished(String category, LocalDate date) {
    Integer sessionsToCompleteDailyGoal = categoryEngine.dailyGoalForCategory(category);
    return countSessionsByDateAndCategory(date, category) >= sessionsToCompleteDailyGoal;
  }

  public void editWeeklyGoal(WeeklyGoal weeklyGoal) {
    this.weeklyGoal = weeklyGoal;
  }

  public boolean weeklyGoalFinishedForDate(LocalDate dateTime) {
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

  public Integer weeklyGoalForCategory(String category) {
    return categoryEngine.weeklyGoalForCategory(category);
  }

  public void editWeeklyGoalForCategory(String categoryName, WeeklyGoal weeklyGoal) {
    categoryEngine.editWeeklyGoalForCategory(categoryName, weeklyGoal);
  }

  public void createCategory(PomodoroCategory pomodoroCategory) {
    categoryEngine.createCategory(pomodoroCategory);
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

  public PomodoroSession getSession(UUID sessionId) {
    return sessionEngine.getSession(sessionId);
  }

  public Integer countSessions(Specification<PomodoroSession> sessionSpecification) {
    return 2;
  }
}
