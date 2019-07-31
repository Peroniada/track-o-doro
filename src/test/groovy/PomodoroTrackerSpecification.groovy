import com.sperek.trackodoro.PomodoroSession
import com.sperek.trackodoro.PomodoroSessionBuilder
import com.sperek.trackodoro.PomodoroTracker
import com.sperek.trackodoro.PomodoroTrackerConfig
import com.sperek.trackodoro.category.PomodoroCategory
import com.sperek.trackodoro.goal.DailyGoal
import com.sperek.trackodoro.goal.WeeklyGoal
import spock.lang.Specification

import java.time.LocalDate
import java.time.ZonedDateTime

class PomodoroTrackerSpecification extends Specification {

    private static final CATEGORY_CODING = "Coding"
    private static final CATEGORY_BOOK = "Book"


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
        today()     | CATEGORY_BOOK   || 3
        yesterday() | CATEGORY_BOOK   || 2
        today()     | CATEGORY_CODING || 1
        yesterday() | CATEGORY_CODING || 3
    }

    def "Should achieve daily goal for pomodoros"() {
        setup:
        pomodoroTracker.saveSessions(sessions)
        pomodoroTracker.editDailyGoal(new DailyGoal(4))

        expect:
        pomodoroTracker.dailyPomodoroGoalFinished(LocalDate.now()) == expectedGoalFulfillment

        where:
        sessions               || expectedGoalFulfillment
        sessionCollectionOf(4) || true
        sessionCollectionOf(2) || false
    }

    def "Should achieve daily goal by category"() {
        setup:
        pomodoroTracker.saveSessions(sessions)
        pomodoroTracker.createCategory(new PomodoroCategory(CATEGORY_BOOK, new DailyGoal(2), new WeeklyGoal(5)))
        pomodoroTracker.createCategory(new PomodoroCategory(CATEGORY_CODING, new DailyGoal(2), new WeeklyGoal(5)))

        expect:
        pomodoroTracker.dailyPomodoroGoalForCategoryFinished(CATEGORY_BOOK, LocalDate.now()) == dailyBookGoalfinished
        pomodoroTracker.dailyPomodoroGoalForCategoryFinished(CATEGORY_CODING, LocalDate.now()) == dailyCodingGoalFinished

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
        pomodoroTracker.setWeeklyGoal(new WeeklyGoal(5))

        expect:
        pomodoroTracker.weeklyGoalFinishedForDate(ZonedDateTime.now()) == weeklyGoalFinished

        where:
        sessions                 || weeklyGoalFinished
        threeCompletedThisWeek() || false
        sevenCompletedThisWeek() || true
    }

    def "Daily Goal should be created for a particular User"() {
        given:
        def dailyGoalNumberOfSessions = 3

        when:
        pomodoroTracker.editDailyGoal(new DailyGoal(dailyGoalNumberOfSessions))

        then:
        pomodoroTracker.userDailyGoalSessionsNumber() == dailyGoalNumberOfSessions
    }

    def "Should create category with weekly goal"() {
        given:
        def numberOfSessionsToFulfill = 10
        when:
        pomodoroTracker.createCategory(new PomodoroCategory("book", new DailyGoal(1), new WeeklyGoal(numberOfSessionsToFulfill)))

        then:
        def categoriesCreatedByUser = pomodoroTracker.categoriesCreatedByUser() as List<PomodoroCategory>
        categoriesCreatedByUser.any { category -> category.sessionsToCompleteWeeklyGoal() == numberOfSessionsToFulfill }

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
                bookSessionBuilder().withOccurrence(dateMinusDays.call(0)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(0)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(0)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(0)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(0)).build(),
                bookSessionBuilder().withOccurrence(dateMinusDays.call(0)).build(),
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
