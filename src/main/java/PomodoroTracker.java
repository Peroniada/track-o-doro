import java.time.LocalDate;
import java.util.Collection;

public class PomodoroTracker {

  private PomodoroSessionRepository sessionRepository;

  PomodoroTracker(PomodoroSessionRepository sessionRepository) {
    this.sessionRepository = sessionRepository;
  }

  public void saveSession(PomodoroSession session) {
    sessionRepository.save(session);
  }

  public Collection<PomodoroSession> allSessions() {
    return sessionRepository.findAll();
  }

  public Collection<PomodoroSession> sessionsByCategory(String category) {
    return sessionRepository.findByCategory(category);
  }

  public void saveSessions(Collection<PomodoroSession> sessions) {
    sessionRepository.saveAll(sessions);
  }

  public Integer countSessionsByCategory(String category) {
    return sessionsByCategory(category).size();
  }

  public Integer countSessionsByDay(LocalDate occurrence) {
    return sessionRepository.findByday(occurrence).size();
  }

  public Integer countSessionsByDateAndCategory(LocalDate date, String category) {
    return sessionRepository.findByDayAndCategory(date, category).size();
  }
}
