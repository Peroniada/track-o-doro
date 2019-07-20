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

        def booksPomodoroSession = bookSession().build()
        def codingPomodoroSession = codingSession().build()
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
        List<PomodoroSession> sessions = [bookSession().build(), bookSession().build(), codingSession().build()]
        when:
        pomodoroTracker.saveSessions(sessions)
        then:
        pomodoroTracker.countSessionsByCategory("Book") == 2
        pomodoroTracker.countSessionsByCategory("Coding") == 1
    }

    def "Should count sessions by date"() {
        given:
        List<PomodoroSession> sessions = [yesterdaySession().build(), todaySession().build(), yesterdaySession().build()]

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
        def bookSessions = [yesterdaySession(), yesterdaySession(), todaySession(), todaySession(), todaySession()]
                .collect { session -> session.withCategory("Book").build() }

        def codingSessions = [todaySession(), yesterdaySession(), yesterdaySession(), yesterdaySession()]
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
        given:
        pomodoroTracker.saveSessions(sessions)
        pomodoroTracker.setDailyGoal(4)

        expect:
        pomodoroTracker.dailyPomodoroGoalFinished(LocalDate.now()) == expectedGoalFulfillment

        where:
        sessions                   || expectedGoalFulfillment
        sessionCollectionOf(4)  || true
        sessionCollectionOf(2)  || false

    }

    private Collection<PomodoroSession> sessionCollectionOf(int i) {
        Collection<PomodoroSession> sessions = []
        (1..i).each { sessions.add(todaySession().build()) }
        sessions
    }

    private static PomodoroSessionBuilder defaultSessionBuilder() {
        PomodoroSessionBuilder
                .aPomodoroSession()
                .withActivityName("Book Reading")
                .withCategory("Books")
                .withDuration(25)
                .withOccurrence(ZonedDateTime.now())
                .withId(UUID.randomUUID())
    }

    private static PomodoroSessionBuilder pomodoroSessionWithCategory(String booksCategory) {
        defaultSessionBuilder().withCategory(booksCategory)
    }

    PomodoroSessionBuilder bookSession() {
        final category = "Book"
        pomodoroSessionWithCategory(category)
    }

    PomodoroSessionBuilder codingSession() {
        final category = "Coding"
        pomodoroSessionWithCategory(category)
    }

    PomodoroSessionBuilder yesterdaySession() {
        defaultSessionBuilder().withOccurrence(ZonedDateTime.now().minusDays(1))
    }

    PomodoroSessionBuilder todaySession() {
        defaultSessionBuilder().withOccurrence(ZonedDateTime.now())
    }


}
