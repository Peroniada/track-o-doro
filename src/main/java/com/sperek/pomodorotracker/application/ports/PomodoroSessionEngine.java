package com.sperek.pomodorotracker.application.ports;

import com.sperek.pomodorotracker.domain.sessionFilter.composite.spec.Specification;
import com.sperek.pomodorotracker.domain.tracker.session.PomodoroSession;
import java.util.Collection;
import java.util.UUID;

public interface PomodoroSessionEngine<OwnerId> {

  void saveOne(PomodoroSession session);
  void saveAll(Collection<PomodoroSession> session);
  Collection<PomodoroSession> allUserSessions(OwnerId ownerId);
  Collection<PomodoroSession> findSatisfyingSessions(Specification<PomodoroSession> specification);
  PomodoroSession getSession(UUID sessionId);

}
