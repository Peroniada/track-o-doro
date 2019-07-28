package com.sperek.trackodoro;

import com.sperek.trackodoro.goal.DailyGoal;
import com.sperek.trackodoro.goal.Goal;
import com.sperek.trackodoro.goal.GoalRepository;
import com.sperek.trackodoro.goal.WeeklyGoal;
import com.sperek.trackodoro.sessionFilter.CategorySpecification;
import com.sperek.trackodoro.sessionFilter.DateSpecification;
import com.sperek.trackodoro.sessionFilter.WeekOfYearSpecification;
import com.sperek.trackodoro.sessionFilter.composite.spec.Specification;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PomodoroTracker {

  private PomodoroSessionRepository sessionRepository;
  private GoalRepository<DailyGoal, UUID> dailyGoalRepository;
  private GoalRepository<WeeklyGoal, UUID> weeklyGoalRepository;
  private final Map<String, Integer> categories = new HashMap<>();

  PomodoroTracker(
      PomodoroSessionRepository sessionRepository,
      GoalRepository<DailyGoal, UUID> dailyGoalRepository,
      GoalRepository<WeeklyGoal, UUID> weeklyGoalRepository) {
    this.sessionRepository = sessionRepository;
    this.dailyGoalRepository = dailyGoalRepository;
    this.weeklyGoalRepository = weeklyGoalRepository;
  }

  public void saveSession(PomodoroSession session) {
    sessionRepository.save(session);
  }

  public void saveSessions(Collection<PomodoroSession> sessions) {
    sessionRepository.saveAll(sessions);
  }

  public Collection<PomodoroSession> allSessions() {
    return sessionRepository.findAll();
  }

  public Collection<PomodoroSession> sessionsByCategory(String category) {
    CategorySpecification specification = new CategorySpecification(category);
    return findSatisfyingSessions(specification);
  }

  public Integer countSessionsByCategory(String category) {
    CategorySpecification specification = new CategorySpecification(category);
    return findSatisfyingSessions(specification).size();
  }

  public Integer countSessionsByDay(LocalDate occurrence) {
    DateSpecification specification = new DateSpecification(occurrence);
    return findSatisfyingSessions(specification).size();
  }

  public Integer countSessionsByDateAndCategory(LocalDate date, String category) {
    Specification<PomodoroSession> specification =
        new DateSpecification(date).and(new CategorySpecification(category));
    return findSatisfyingSessions(specification).size();
  }

  public Boolean dailyPomodoroGoalFinished(LocalDate date, UUID userId) {
    return countSessionsByDay(date).equals(userDailyGoalSessionsNumber(userId));
  }

  public boolean dailyPomodoroGoalForCategoryFinished(String category, LocalDate date) {
    return countSessionsByDateAndCategory(date, category).equals(categoryDailyGoal(category));
  }

  private Integer categoryDailyGoal(String category) {
    return categories.get(category);
  }

  public void setDailyGoalForCategory(String category, int goal) {
    categories.putIfAbsent(category, goal);
  }

  public void setWeeklyGoalRepository(WeeklyGoal weeklyGoal) {
    this.weeklyGoalRepository.save(weeklyGoal);
  }

  public boolean weeklyGoalFinishedForDate(ZonedDateTime dateTime, UUID ownerId) {

    final int week = weekNumberOfDate(dateTime);
    final Integer weeklyGoal = weeklyGoalOfUser(ownerId).getNumberOfSessionsToFulfill();
    return weeklyGoal <= countSessionsForWeek(week);
  }

  private Goal weeklyGoalOfUser(UUID ownerId) {
    return this.weeklyGoalRepository.getOne(ownerId);
  }

  public void editDailyGoal(DailyGoal dailyGoal) {
    this.dailyGoalRepository.save(dailyGoal);
  }

  public Integer userDailyGoalSessionsNumber(UUID userId) {
    return dailyGoalOfUser(userId).getNumberOfSessionsToFulfill();
  }

  private Goal dailyGoalOfUser(UUID ownerId) {
    return this.dailyGoalRepository.getOne(ownerId);
  }

  private int weekNumberOfDate(ZonedDateTime dateTime) {
    return dateTime.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
  }

  private Integer countSessionsForWeek(Integer week) {
    return findSatisfyingSessions(new WeekOfYearSpecification(week)).size();
  }

  private List<PomodoroSession> findSatisfyingSessions(
      Specification<PomodoroSession> specification) {
    return allSessions().stream().filter(specification::isSatisfiedBy).collect(Collectors.toList());
  }
}
