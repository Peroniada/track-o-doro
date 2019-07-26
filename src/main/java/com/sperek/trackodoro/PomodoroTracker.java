package com.sperek.trackodoro;

import com.sperek.trackodoro.sessionFilter.CategorySpecification;
import com.sperek.trackodoro.sessionFilter.DateSpecification;
import com.sperek.trackodoro.sessionFilter.WeekOfYearSpecification;
import com.sperek.trackodoro.sessionFilter.composite.spec.Specification;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class PomodoroTracker {

  private PomodoroSessionRepository sessionRepository;
  private Integer dailyGoal;
  private final Map<String, Integer> categories = new HashMap<>();
  private WeeklyGoal weeklyGoal;

  PomodoroTracker(PomodoroSessionRepository sessionRepository) {
    this.sessionRepository = sessionRepository;
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

  public Boolean dailyPomodoroGoalFinished(LocalDate date) {
    return countSessionsByDay(date).equals(this.dailyGoal);
  }

  public boolean dailyPomodoroGoalForCategoryFinished(String category, LocalDate date) {
    return countSessionsByDateAndCategory(date,category).equals(categoryDailyGoal(category));
  }

  private Integer categoryDailyGoal(String category) {
    return categories.get(category);
  }

  public void setDailyGoalForCategory(String category, int goal) {
      categories.putIfAbsent(category, goal);
  }

  public void setDailyGoal(Integer dailyGoal) {
    this.dailyGoal = dailyGoal;
  }

  public void setWeeklyGoal(WeeklyGoal weeklyGoal) {
    this.weeklyGoal = weeklyGoal;
  }

  public boolean weeklyGoalFinishedForDate(ZonedDateTime dateTime) {

    final int week = weekNumberOfDate(dateTime);
    final Integer weeklyGoal = this.weeklyGoal.getGoal();
    return weeklyGoal.equals(countSessionsForWeek(week));
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
