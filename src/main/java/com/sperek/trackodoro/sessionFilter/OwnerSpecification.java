package com.sperek.trackodoro.sessionFilter;

import com.sperek.trackodoro.PomodoroSession;
import com.sperek.trackodoro.sessionFilter.composite.spec.Specification;

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
