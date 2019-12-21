package com.sperek.pomodorotracker.domain.sessionFilter;

import com.sperek.pomodorotracker.domain.sessionFilter.composite.spec.Specification;
import com.sperek.pomodorotracker.domain.tracker.session.PomodoroSession;

import java.time.LocalDate;

public class DateSpecification implements Specification<PomodoroSession> {

  private final LocalDate expectedDate;

  public DateSpecification(LocalDate expectedDate) {
    this.expectedDate = expectedDate;
  }


  @Override
  public boolean isSatisfiedBy(PomodoroSession pomodoroSession) {
    LocalDate pomodoroSessionOccurence = pomodoroSession.getOccurrence().toLocalDate();
    return this.expectedDate.equals(pomodoroSessionOccurence);
  }
}
