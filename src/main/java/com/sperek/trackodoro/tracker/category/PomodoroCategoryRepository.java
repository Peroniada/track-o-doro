package com.sperek.trackodoro.tracker.category;

import com.sperek.trackodoro.category.PomodoroCategory;
import java.util.Collection;

public interface PomodoroCategoryRepository {

  PomodoroCategory findByName(String name);

  void save(PomodoroCategory pomodoroCategory);

  Collection<PomodoroCategory> findAll();

  void delete(String category);

}
