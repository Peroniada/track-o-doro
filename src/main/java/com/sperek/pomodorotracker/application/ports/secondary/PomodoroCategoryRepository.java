package com.sperek.pomodorotracker.application.ports.secondary;

import com.sperek.pomodorotracker.domain.model.PomodoroCategory;
import java.util.Collection;
import java.util.UUID;

public interface PomodoroCategoryRepository {

  PomodoroCategory findByName(UUID ownerId, String name);

  void save(PomodoroCategory pomodoroCategory);

  Collection<PomodoroCategory> findAll(UUID ownerId);

  void delete(UUID ownerId, String category);

}
