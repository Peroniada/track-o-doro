import java.time.ZonedDateTime;
import java.util.UUID;

public class PomodoroSession {

    private final String activityName;
    private final String category;
    private final Integer duration;
    private final ZonedDateTime occurrence;
    private final UUID id;

    public PomodoroSession(String activityName, String category, Integer duration,
        ZonedDateTime occurrence, UUID id) {
        this.activityName = activityName;
        this.category = category;
        this.duration = duration;
        this.occurrence = occurrence;
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getCategory() {
        return category;
    }

    public Integer getDuration() {
        return duration;
    }

    public ZonedDateTime getOccurrence() {
        return occurrence;
    }

    public UUID getId() {
        return id;
    }
}
