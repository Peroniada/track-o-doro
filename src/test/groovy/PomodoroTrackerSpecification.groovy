import com.sperek.trackodoro.PomodoroSession
import com.sperek.trackodoro.PomodoroSessionBuilder
import com.sperek.trackodoro.PomodoroTracker
import com.sperek.trackodoro.PomodoroTrackerConfig
import spock.lang.Specification

import java.time.LocalDate
import java.time.ZonedDateTime

class PomodoroTrackerSpecification extends Specification {

    PomodoroTracker pomodoroTracker

    def setup() {
        setup:
        pomodoroTracker = PomodoroTrackerConfig.pomodoroTracker()
    }

    def "Should save a pomodoro session"() {
        given:
        def pomodoroSession = defaultSessionBuilder().build()

        when:
        pomodoroTracker.saveSession(pomodoroSession)

        then:
        pomodoroTracker.allSessions().contains(pomodoroSession)
    }

    def "Should return a sessions with given category"() {
        given:

        def booksPomodoroSession = bookSessionBuilder().build()
        def codingPomodoroSession = codingSessionBuilder().build()
        when:
        pomodoroTracker.saveSession(booksPomodoroSession)
        pomodoroTracker.saveSession(codingPomodoroSession)

        def booksCategory = "Book"
        def sessionsByCategory = pomodoroTracker.sessionsByCategory(booksCategory)
        then:
        sessionsByCategory.size() == 1
        sessionsByCategory.first().getCategory() == booksCategory
    }

    def "Should count sessions by category"() {
        given:
        List<PomodoroSession> sessions = [bookSessionBuilder().build(), bookSessionBuilder().build(), codingSessionBuilder().build()]
        when:
        pomodoroTracker.saveSessions(sessions)
        then:
        pomodoroTracker.countSessionsByCategory("Book") == 2
        pomodoroTracker.countSessionsByCategory("Coding") == 1
    }

    def "Should count sessions by date"() {
        given:
        List<PomodoroSession> sessions = [yesterdaySessionBuilder().build(), todaySessionBuilder().build(), yesterdaySessionBuilder().build()]

        when:
        pomodoroTracker.saveSessions(sessions)

        def yesterday = LocalDate.now().minusDays(1)
        def today = LocalDate.now()
        then:
        pomodoroTracker.countSessionsByDay(yesterday) == 2
        pomodoroTracker.countSessionsByDay(today) == 1
    }

    def "Should count sessions by category and date"() {
        given:
        def bookSessions = [yesterdaySessionBuilder(), yesterdaySessionBuilder(), todaySessionBuilder(), todaySessionBuilder(), todaySessionBuilder()]
                .collect { session -> session.withCategory("Book").build() }

        def codingSessions = [todaySessionBuilder(), yesterdaySessionBuilder(), yesterdaySessionBuilder(), yesterdaySessionBuilder()]
                .collect { session -> session.withCategory("Coding").build() }

        List<PomodoroSession> sessions = []
        sessions.addAll(bookSessions)
        sessions.addAll(codingSessions)

        when:
        pomodoroTracker.saveSessions(sessions)

        def yesterday = LocalDate.now().minusDays(1)
        then:
        pomodoroTracker.countSessionsByDateAndCategory(yesterday, "Book") == 2
        pomodoroTracker.countSessionsByDateAndCategory(yesterday, "Coding") == 3

    }

    def "Should achieve daily goal for pomodoros"() {
        setup:
        pomodoroTracker.saveSessions(sessions)
        pomodoroTracker.setDailyGoal(4)

        expect:
        pomodoroTracker.dailyPomodoroGoalFinished(LocalDate.now()) == expectedGoalFulfillment

        where:
        sessions                   || expectedGoalFulfillment
        sessionCollectionOf(4)  || true
        sessionCollectionOf(2)  || false

    }

    def "Should achieve daily goal by category"() {
        setup:
        pomodoroTracker.saveSessions(sessions)
        def booksCategory = "Books"
        def codingCategory = "Coding"
        pomodoroTracker.setDailyGoalForCategory(booksCategory, 2)
        pomodoroTracker.setDailyGoalForCategory(codingCategory, 2)

        expect:
        pomodoroTracker.dailyPomodoroGoalForCategoryFinished(booksCategory, LocalDate.now()) == expectedDailyBooksGoal
        pomodoroTracker.dailyPomodoroGoalForCategoryFinished(codingCategory, LocalDate.now()) == expectedDailyCodingGoal

        where:
        sessions                        || expectedDailyBooksGoal || expectedDailyCodingGoal
        booksFinishedCodingFailed()     || true                   || false
        booksFailedCodingFinished()     || false                  || true
        booksFinishedCodingFinished()   || true                   || true
        booksFailedCodingFailed()       || false                  || false

    }

    private static List<PomodoroSession> booksFailedCodingFailed() {
        [todayBookSession(), yesterdayBookSession(), todayCodingSession(), yesterdayCodingSession()]
    }

    private static List<PomodoroSession> booksFinishedCodingFinished() {
        [todayBookSession(), todayBookSession(), yesterdayBookSession()]
    }

    private static List<PomodoroSession> booksFailedCodingFinished() {
        [todayCodingSession(), todayCodingSession(), yesterdayBookSession()]
    }

    private static List<PomodoroSession> booksFinishedCodingFailed() {
        [todayBookSession(), todayBookSession(), yesterdayBookSession()]
    }

    private static PomodoroSession yesterdayCodingSession() {
        codingSessionBuilder().withOccurrence(yesterday()).build()
    }

    private static PomodoroSession yesterdayBookSession() {
        bookSessionBuilder().withOccurrence(yesterday()).build()
    }

    private static PomodoroSession todayCodingSession() {
        codingSessionBuilder().build()
    }

    private static PomodoroSession todayBookSession() {
        bookSessionBuilder().build()
    }

    private static Collection<PomodoroSession> sessionCollectionOf(int i) {
        Collection<PomodoroSession> sessions = []
        (1..i).each { sessions.add(todaySessionBuilder().build()) }
        sessions
    }

    private static PomodoroSessionBuilder yesterdaySessionBuilder() {
        defaultSessionBuilder().withOccurrence(yesterday())
    }

    private static PomodoroSessionBuilder todaySessionBuilder() {
        pomodoroSessionBuilderWithOccurrence(today())
    }

    private static PomodoroSessionBuilder codingSessionBuilder() {
        final category = "Coding"
        pomodoroSessionBuilderWithCategory(category)
    }

    private static PomodoroSessionBuilder bookSessionBuilder() {
        final category = "Book"
        pomodoroSessionBuilderWithCategory(category)
    }

    private static PomodoroSessionBuilder pomodoroSessionBuilderWithOccurrence(ZonedDateTime dateTime) {
        defaultSessionBuilder().withOccurrence(dateTime)
    }

    private static PomodoroSessionBuilder pomodoroSessionBuilderWithCategory(String booksCategory) {
        defaultSessionBuilder().withCategory(booksCategory)
    }

    private static PomodoroSessionBuilder defaultSessionBuilder() {
        PomodoroSessionBuilder
                .aPomodoroSession()
                .withActivityName("Book Reading")
                .withCategory("Book")
                .withDuration(25)
                .withOccurrence(ZonedDateTime.now())
                .withId(UUID.randomUUID())
    }

    private static ZonedDateTime yesterday() {
        ZonedDateTime.now().minusDays(1)
    }

    private static ZonedDateTime today() {
        ZonedDateTime.now()
    }

}
