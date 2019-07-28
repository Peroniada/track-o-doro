import com.sperek.trackodoro.*
import com.sperek.trackodoro.goal.DailyGoal
import com.sperek.trackodoro.goal.WeeklyGoal
import spock.lang.Specification

import java.time.LocalDate
import java.time.ZonedDateTime

class PomodoroTrackerSpecification extends Specification {

    private static final CATEGORY_CODING = "Coding"
    private static final CATEGORY_BOOK = "Book"
    private static final userId = UUID.fromString("2d5230d2-fe9e-4b67-aec8-88ac8a463a91")


    private PomodoroTracker pomodoroTracker
    private static final Closure<ZonedDateTime> dateMinusDays = {
        Long days -> ZonedDateTime.now().minusDays(days)
    }

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
        def booksPomodoroSession = todayBookSession()
        def codingPomodoroSession = todayCodingSession()

        when:
        pomodoroTracker.saveSession(booksPomodoroSession)
        pomodoroTracker.saveSession(codingPomodoroSession)
        def booksCategory = CATEGORY_BOOK
        def sessionsByCategory = pomodoroTracker.sessionsByCategory(booksCategory)

        then:
        sessionsByCategory.size() == 1
        sessionsByCategory.first().getCategory() == booksCategory
    }

    def "Should count sessions by category"() {
        given:
        def sessions = [todayBookSession(), todayBookSession(), todayCodingSession()]

        when:
        pomodoroTracker.saveSessions(sessions)

        then:
        pomodoroTracker.countSessionsByCategory(CATEGORY_BOOK) == 2
        pomodoroTracker.countSessionsByCategory(CATEGORY_CODING) == 1
    }

    def "Should count sessions by date"() {
        given:
        def sessions = [yesterdayBookSession(), todayBookSession(), yesterdayCodingSession()]

        when:
        pomodoroTracker.saveSessions(sessions)

        then:
        def yesterday = yesterdayDateTime().toLocalDate()
        def today = todayDateTime().toLocalDate()
        pomodoroTracker.countSessionsByDay(yesterday) == 2
        pomodoroTracker.countSessionsByDay(today) == 1
    }

    def "Should count sessions by category and date"() {
        setup:
        def bookSessions = [yesterdayBookSession(), yesterdayBookSession(), todayBookSession(), todayBookSession(), todayBookSession()]
        def codingSessions = [todayCodingSession(), yesterdayCodingSession(), yesterdayCodingSession(), yesterdayCodingSession()]

        List<PomodoroSession> sessions = []
        sessions.addAll(bookSessions)
        sessions.addAll(codingSessions)
        pomodoroTracker.saveSessions(sessions)

        expect:
        pomodoroTracker.countSessionsByDateAndCategory(day, category) == expectedNumberOfSessions

        where:
        day         | category        || expectedNumberOfSessions
        today()     | CATEGORY_BOOK   ||                        3
        yesterday() | CATEGORY_BOOK   ||                        2
        today()     | CATEGORY_CODING ||                        1
        yesterday() | CATEGORY_CODING ||                        3
    }

    def "Should achieve daily goal for pomodoros"() {
        setup:
        pomodoroTracker.saveSessions(sessions)
        pomodoroTracker.editDailyGoal(new DailyGoal(userId, 4))

        expect:
        pomodoroTracker.dailyPomodoroGoalFinished(LocalDate.now(), userId) == expectedGoalFulfillment

        where:
        sessions               || expectedGoalFulfillment
        sessionCollectionOf(4) || true
        sessionCollectionOf(2) || false
    }

    def "Should achieve daily goal by category"() {
        setup:
        pomodoroTracker.saveSessions(sessions)
        def booksCategory = CATEGORY_BOOK
        def codingCategory = CATEGORY_CODING
        pomodoroTracker.setDailyGoalForCategory(booksCategory, 2)
        pomodoroTracker.setDailyGoalForCategory(codingCategory, 2)

        expect:
        pomodoroTracker.dailyPomodoroGoalForCategoryFinished(booksCategory, LocalDate.now()) == dailyBookGoalfinished
        pomodoroTracker.dailyPomodoroGoalForCategoryFinished(codingCategory, LocalDate.now()) == dailyCodingGoalFinished

        where:
        sessions                      || dailyBookGoalfinished || dailyCodingGoalFinished
        booksFinishedCodingFailed()   || true                  || false
        booksFailedCodingFinished()   || false                 || true
        booksFinishedCodingFinished() || true                  || true
        booksFailedCodingFailed()     || false                 || false

    }

    def "Should achieve weekly goal of pomodoros"() {
        setup:
        pomodoroTracker.saveSessions(sessions)
        pomodoroTracker.setWeeklyGoalRepository(new WeeklyGoal(5, userId))

        expect:
        pomodoroTracker.weeklyGoalFinishedForDate(ZonedDateTime.now(),userId) == weeklyGoalFinished

        where:
        sessions                 || weeklyGoalFinished
        threeCompletedThisWeek() || false
        sevenCompletedThisWeek() || true
    }

    def "Daily Goal should be created for a particular User"() {
        given: "A user id: " + userId
        def dailyGoalNumberOfSessions = 3

        when:
        pomodoroTracker.editDailyGoal(new DailyGoal(userId, dailyGoalNumberOfSessions))

        then:
        pomodoroTracker.userDailyGoalSessionsNumber(userId) == dailyGoalNumberOfSessions
    }

    private static List<PomodoroSession> booksFailedCodingFailed() {
        [todayBookSession(), yesterdayBookSession(), todayCodingSession(), yesterdayCodingSession()]
    }

    private static List<PomodoroSession> booksFinishedCodingFinished() {
        [todayBookSession(), todayBookSession(), todayCodingSession(), todayCodingSession()]
    }

    private static List<PomodoroSession> booksFailedCodingFinished() {
        [todayCodingSession(), todayCodingSession(), yesterdayBookSession()]
    }

    private static List<PomodoroSession> booksFinishedCodingFailed() {
        [todayBookSession(), todayBookSession(), yesterdayBookSession()]
    }

    private static PomodoroSession yesterdayCodingSession() {
        codingSessionBuilder().withOccurrence(yesterdayDateTime()).build()
    }

    private static PomodoroSession yesterdayBookSession() {
        bookSessionBuilder().withOccurrence(yesterdayDateTime()).build()
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

    private static List<PomodoroSession> sevenCompletedThisWeek() {
        [
                bookSessionBuilder().withOccurrence(dateMinusDays.call(6)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(5)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(4)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(3)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(2)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(1)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(0)).build(),
        ]
    }

    private static List<PomodoroSession> threeCompletedThisWeek() {
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

    private static PomodoroSessionBuilder todaySessionBuilder() {
        pomodoroSessionBuilderWithOccurrence(todayDateTime())
    }

    private static PomodoroSessionBuilder codingSessionBuilder() {
        pomodoroSessionBuilderWithCategory(CATEGORY_CODING)
    }

    private static PomodoroSessionBuilder bookSessionBuilder() {
        pomodoroSessionBuilderWithCategory(CATEGORY_BOOK)
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
                .withCategory(CATEGORY_BOOK)
                .withDuration(25)
                .withOccurrence(ZonedDateTime.now())
                .withId(UUID.randomUUID())
    }

    private static LocalDate yesterday() {
        yesterdayDateTime().toLocalDate()
    }

    private static LocalDate today() {
        todayDateTime().toLocalDate()
    }

    private static ZonedDateTime yesterdayDateTime() {
        ZonedDateTime.now().minusDays(1)
    }

    private static ZonedDateTime todayDateTime() {
        ZonedDateTime.now()
    }

}
