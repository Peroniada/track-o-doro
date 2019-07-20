package com.sperek.trackodoro;

import java.time.LocalDate;
import java.util.Collection;

public interface PomodoroSessionRepository {

  PomodoroSession save(PomodoroSession session);

  Collection<PomodoroSession> findAll();

  Collection<PomodoroSession> saveAll(Collection<PomodoroSession> sessions);

}
