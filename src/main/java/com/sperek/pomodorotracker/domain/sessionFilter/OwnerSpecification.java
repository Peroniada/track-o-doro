package com.sperek.pomodorotracker.domain.sessionFilter;

import com.sperek.pomodorotracker.domain.sessionFilter.composite.spec.Specification;
import com.sperek.pomodorotracker.domain.tracker.session.PomodoroSession;

import java.util.UUID;

public class OwnerSpecification implements Specification<PomodoroSession> {

  private final UUID ownerId;

  public OwnerSpecification(UUID ownerId) {
    this.ownerId = ownerId;
  }

  @Override
  public boolean isSatisfiedBy(PomodoroSession pomodoroSession) {
    return ownerId.equals(pomodoroSession.sessionsOwner());
  }
}
