import java.time.LocalDate;
import java.util.Collection;

public interface PomodoroSessionRepository {

  PomodoroSession save(PomodoroSession session);

  Collection<PomodoroSession> findAll();

  Collection<PomodoroSession> findByCategory(String category);

  Collection<PomodoroSession> saveAll(Collection<PomodoroSession> sessions);

  Collection<PomodoroSession> findByday(LocalDate date);

  Collection<PomodoroSession> findByDayAndCategory(LocalDate date, String category);
}
