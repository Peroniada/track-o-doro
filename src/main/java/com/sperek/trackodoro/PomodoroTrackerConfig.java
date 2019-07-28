package com.sperek.trackodoro;

import com.sperek.trackodoro.goal.InMemoryGoalRepository;

public class PomodoroTrackerConfig  {


  public static PomodoroTracker pomodoroTracker() {
    return new PomodoroTracker(new InMemoryPomodoroSessionRepository(),
        new InMemoryGoalRepository<>(), new InMemoryGoalRepository<>());
  }
}
