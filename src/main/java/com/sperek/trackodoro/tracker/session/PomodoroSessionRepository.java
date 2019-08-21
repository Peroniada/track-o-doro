package com.sperek.trackodoro.tracker.session;

import com.sperek.trackodoro.tracker.session.PomodoroSession;
import java.util.Collection;
import java.util.UUID;

public interface PomodoroSessionRepository {

  PomodoroSession save(PomodoroSession session);

  Collection<PomodoroSession> findAll();

  Collection<PomodoroSession> saveAll(Collection<PomodoroSession> sessions);

  PomodoroSession getOne(UUID sessionId);
}
