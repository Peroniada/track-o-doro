package com.sperek.trackodoro;

import com.sperek.trackodoro.sessionFilter.CategorySpecification;
import com.sperek.trackodoro.sessionFilter.DateSpecification;
import com.sperek.trackodoro.sessionFilter.composite.spec.Specification;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PomodoroTracker {

  private PomodoroSessionRepository sessionRepository;
  private Integer dailyGoal;

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

  public void setDailyGoal(Integer dailyGoal) {
    this.dailyGoal = dailyGoal;
  }

  private List<PomodoroSession> findSatisfyingSessions(
      Specification<PomodoroSession> specification) {
    return allSessions().stream().filter(specification::isSatisfiedBy).collect(Collectors.toList());
  }
}
