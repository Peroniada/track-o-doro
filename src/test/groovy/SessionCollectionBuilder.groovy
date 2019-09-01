import com.sperek.trackodoro.tracker.session.PomodoroSession
import com.sperek.trackodoro.tracker.session.PomodoroSessionBuilder

import java.time.ZonedDateTime

import static SessionsDSL.defaultSessionBuilder

class SessionCollectionBuilder {

    PomodoroSessionBuilder builder = defaultSessionBuilder()

    def withCategory(String category) {
        builder.withCategory(category)
        this
    }

    def withOccurrence(ZonedDateTime dateTime) {
        builder.withOccurrence(dateTime)
        this
    }

    def withName(String name) {
        builder.withActivityName(name)
        this
    }

    List<PomodoroSession> getAmountOf(int amount) {
        def sessions = []
        (1..amount).each {sessions.add(builder.withId(UUID.randomUUID()).build())}
        sessions as List<PomodoroSession>
    }





}
