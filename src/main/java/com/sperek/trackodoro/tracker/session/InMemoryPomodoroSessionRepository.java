package com.sperek.trackodoro.tracker.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryPomodoroSessionRepository implements PomodoroSessionRepository {

  private final Map<UUID, PomodoroSession> pomodoroSessions = new HashMap<>();

  @Override
  public PomodoroSession save(PomodoroSession session) {
    pomodoroSessions.put(session.getId(), session);
    return session;
  }

  @Override
  public Collection<PomodoroSession> findAll() {
    return new ArrayList<>(pomodoroSessions.values());
  }

  @Override
  public Collection<PomodoroSession> saveAll(Collection<PomodoroSession> sessions) {
    sessions.forEach(this::save);
    return sessions;
  }

  @Override
  public PomodoroSession getOne(UUID sessionId) {
    return pomodoroSessions.get(sessionId);
  }

}
