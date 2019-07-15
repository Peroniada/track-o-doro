public class PomodoroTrackerConfig  {


  public static PomodoroTracker pomodoroTracker() {
    return new PomodoroTracker(new InMemoryPomodoroSessionRepository());
  }
}
