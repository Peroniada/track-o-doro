import com.sperek.trackodoro.PomodoroSession
import com.sperek.trackodoro.PomodoroSessionBuilder

import java.time.LocalDate
import java.time.ZonedDateTime

class SessionsDSL {
    static final CATEGORY_CODING = "Coding"
    static final CATEGORY_BOOK = "Book"
    static final Closure<ZonedDateTime> dateMinusDays = { Long days -> ZonedDateTime.now().minusDays(days) }
    static final UUID ownerId = UUID.fromString("37868076-7232-4067-bb27-2d2e00acf390")
    
    static List<PomodoroSession> booksFailedCodingFailed() {
        [todayBookSession(), yesterdayBookSession(), todayCodingSession(), yesterdayCodingSession()]
    }

    static List<PomodoroSession> booksFinishedCodingFinished() {
        [todayBookSession(), todayBookSession(), todayCodingSession(), todayCodingSession()]
    }

    static List<PomodoroSession> booksFailedCodingFinished() {
        [todayCodingSession(), todayCodingSession(), yesterdayBookSession()]
    }

    static List<PomodoroSession> booksFinishedCodingFailed() {
        [todayBookSession(), todayBookSession(), yesterdayBookSession()]
    }

    static PomodoroSession yesterdayCodingSession() {
        codingSessionBuilder().withOccurrence(yesterdayDateTime()).build()
    }

    static PomodoroSession yesterdayBookSession() {
        bookSessionBuilder().withOccurrence(yesterdayDateTime()).build()
    }

    static PomodoroSession todayCodingSession() {
        codingSessionBuilder().build()
    }

    static PomodoroSession todayBookSession() {
        bookSessionBuilder().build()
    }

    static Collection<PomodoroSession> sessionCollectionOf(int i) {
        Collection<PomodoroSession> sessions = []
        (1..i).each { sessions.add(todaySessionBuilder().build()) }
        sessions
    }

    static List<PomodoroSession> sevenCompletedThisWeek() {
        [
                bookSessionBuilder().withOccurrence(dateMinusDays.call(0)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(0)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(0)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(0)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(0)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(0)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(0)).build(),
        ]
    }

    static List<PomodoroSession> threeCompletedThisWeek() {
        [
                bookSessionBuilder().withOccurrence(dateMinusDays.call(10L)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(10L)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(9L)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(7L)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(7L)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(7L)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(7L)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(7L)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(3L)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(2L)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(2L)).build()
        ]
    }

    static PomodoroSessionBuilder todaySessionBuilder() {
        pomodoroSessionBuilderWithOccurrence(todayDateTime())
    }

    static PomodoroSessionBuilder codingSessionBuilder() {
        pomodoroSessionBuilderWithCategory(CATEGORY_CODING)
    }

    static PomodoroSessionBuilder bookSessionBuilder() {
        pomodoroSessionBuilderWithCategory(CATEGORY_BOOK)
    }

    static PomodoroSessionBuilder pomodoroSessionBuilderWithOccurrence(ZonedDateTime dateTime) {
        defaultSessionBuilder().withOccurrence(dateTime)
    }

    static PomodoroSessionBuilder pomodoroSessionBuilderWithCategory(String booksCategory) {
        defaultSessionBuilder().withCategory(booksCategory)
    }

    static PomodoroSessionBuilder defaultSessionBuilder() {
        PomodoroSessionBuilder
                .aPomodoroSession()
                .withActivityName("Book Reading")
                .withCategory(CATEGORY_BOOK)
                .withDuration(25)
                .withOccurrence(ZonedDateTime.now())
                .withId(UUID.randomUUID())
                .withOwnerId(ownerId)
    }

    static LocalDate yesterday() {
        yesterdayDateTime().toLocalDate()
    }

    static LocalDate today() {
        todayDateTime().toLocalDate()
    }

    static ZonedDateTime yesterdayDateTime() {
        ZonedDateTime.now().minusDays(1)
    }

    static ZonedDateTime todayDateTime() {
        ZonedDateTime.now()
    }
}
