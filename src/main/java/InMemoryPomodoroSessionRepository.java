import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
  public Collection<PomodoroSession> findByCategory(String category) {
    return findAll().stream().filter(pomodoroCategoryPredicate(category)).collect(
        Collectors.toList());
  }

  @Override
  public Collection<PomodoroSession> saveAll(Collection<PomodoroSession> sessions) {
    sessions.forEach(this::save);
    return sessions;
  }

  @Override
  public Collection<PomodoroSession> findByday(LocalDate occurrence) {
    return findAll().stream().filter(pomodoroDatePredicate(occurrence)).collect(
        Collectors.toList());
  }

  @Override
  public Collection<PomodoroSession> findByDayAndCategory(LocalDate date, String category) {
    return findByday(date).stream().filter(pomodoroCategoryPredicate(category))
        .collect(Collectors.toList());
  }

  private Predicate<PomodoroSession> pomodoroCategoryPredicate(String category) {
    return session -> category.equals(session.getCategory());
  }

  private Predicate<PomodoroSession> pomodoroDatePredicate(LocalDate localDate) {
    return session -> localDate.equals(session.getOccurrence().toLocalDate());
  }
}
