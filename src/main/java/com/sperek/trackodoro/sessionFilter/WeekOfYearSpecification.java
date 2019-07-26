package com.sperek.trackodoro.sessionFilter;

import com.sperek.trackodoro.PomodoroSession;
import com.sperek.trackodoro.sessionFilter.composite.spec.Specification;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class WeekOfYearSpecification implements Specification<PomodoroSession> {

  private final Integer weekNumber;

  public WeekOfYearSpecification(Integer weekNumber) {
    this.weekNumber = weekNumber;
  }

  @Override
  public boolean isSatisfiedBy(PomodoroSession pomodoroSession) {
    final ZonedDateTime occurrence = pomodoroSession.getOccurrence();
    return weekNumber.equals(sessionOccurrenceWeekNumber(occurrence));
  }

  private int sessionOccurrenceWeekNumber(ZonedDateTime occurrence) {
    return occurrence.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
  }
}
