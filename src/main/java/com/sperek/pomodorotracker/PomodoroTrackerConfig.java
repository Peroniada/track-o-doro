package com.sperek.pomodorotracker;


import com.sperek.pomodorotracker.domain.tracker.PomodoroTracker;
import com.sperek.pomodorotracker.domain.tracker.category.InMemoryPomodoroCategoryRepository;
import com.sperek.pomodorotracker.domain.tracker.category.PomodoroCategoryEngineImpl;
import com.sperek.pomodorotracker.domain.tracker.session.InMemoryPomodoroSessionRepository;
import com.sperek.pomodorotracker.domain.tracker.session.PomodoroSessionEngineImpl;

public class PomodoroTrackerConfig  {

  public static PomodoroTracker pomodoroTracker() {
    return new PomodoroTracker(new PomodoroSessionEngineImpl(new InMemoryPomodoroSessionRepository()), new PomodoroCategoryEngineImpl(new InMemoryPomodoroCategoryRepository()));
  }
}
