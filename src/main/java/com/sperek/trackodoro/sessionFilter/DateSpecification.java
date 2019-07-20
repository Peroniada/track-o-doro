package com.sperek.trackodoro.sessionFilter;

import com.sperek.trackodoro.PomodoroSession;
import com.sperek.trackodoro.sessionFilter.composite.spec.Specification;

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
