package com.sperek.trackodoro.tracker;


import com.sperek.trackodoro.tracker.category.InMemoryPomodoroCategoryRepository;
import com.sperek.trackodoro.tracker.category.PomodoroCategoryEngineImpl;
import com.sperek.trackodoro.tracker.session.InMemoryPomodoroSessionRepository;
import com.sperek.trackodoro.tracker.session.PomodoroSessionEngineImpl;

public class PomodoroTrackerConfig  {

  public static PomodoroTracker pomodoroTracker() {
    return new PomodoroTracker(new PomodoroSessionEngineImpl(new InMemoryPomodoroSessionRepository()), new PomodoroCategoryEngineImpl(new InMemoryPomodoroCategoryRepository()));
  }
}
