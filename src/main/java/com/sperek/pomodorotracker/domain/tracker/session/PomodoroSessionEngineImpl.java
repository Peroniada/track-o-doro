package com.sperek.pomodorotracker.domain.tracker.session;

import com.sperek.pomodorotracker.application.ports.PomodoroSessionEngine;
import com.sperek.pomodorotracker.application.ports.secondary.PomodoroSessionRepository;
import com.sperek.pomodorotracker.domain.sessionFilter.composite.spec.Specification;
import com.sperek.pomodorotracker.domain.sessionFilter.OwnerSpecification;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class PomodoroSessionEngineImpl implements PomodoroSessionEngine<UUID> {

  private PomodoroSessionRepository sessionRepository;

  public PomodoroSessionEngineImpl(PomodoroSessionRepository sessionRepository) {
    this.sessionRepository = sessionRepository;
  }

  @Override
  public void saveOne(PomodoroSession session) {
    this.sessionRepository.save(session);
  }

  @Override
  public void saveAll(Collection<PomodoroSession> sessions) {
    this.sessionRepository.saveAll(sessions);
  }

  @Override
  public Collection<PomodoroSession> allUserSessions(UUID ownerId) {
    return findSatisfyingSessions(new OwnerSpecification(ownerId));
  }

  @Override
  public Collection<PomodoroSession> findSatisfyingSessions(
      Specification<PomodoroSession> specification) {
    return this.sessionRepository.findAll().stream().filter(specification::isSatisfiedBy).collect(
        Collectors.toList());
  }

  @Override
  public PomodoroSession getSession(UUID sessionId) {
    return sessionRepository.getOne(sessionId);
  }
}
