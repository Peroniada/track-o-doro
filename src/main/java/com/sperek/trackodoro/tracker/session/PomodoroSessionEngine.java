package com.sperek.trackodoro.tracker.session;

import com.sperek.trackodoro.sessionFilter.composite.spec.Specification;
import java.util.Collection;
import java.util.UUID;

public interface PomodoroSessionEngine<OwnerId> {

  void saveOne(PomodoroSession session);
  void saveAll(Collection<PomodoroSession> session);
  Collection<PomodoroSession> allUserSessions(OwnerId ownerId);
  Collection<PomodoroSession> findSatisfyingSessions(Specification<PomodoroSession> specification);
  PomodoroSession getSession(UUID sessionId);

}
