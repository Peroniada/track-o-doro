import com.sperek.trackodoro.PomodoroSession
import com.sperek.trackodoro.PomodoroTracker
import com.sperek.trackodoro.PomodoroTrackerConfig
import com.sperek.trackodoro.category.PomodoroCategory
import com.sperek.trackodoro.goal.DailyGoal
import com.sperek.trackodoro.goal.WeeklyGoal
import spock.lang.Specification

import java.time.LocalDate
import java.time.ZonedDateTime

import static SessionsDSL.*

class PomodoroTrackerSpecification extends Specification {

    private final UUID ownerId = UUID.fromString("37868076-7232-4067-bb27-2d2e00acf390")

    private PomodoroTracker pomodoroTracker

    def setup() {
        setup:
        pomodoroTracker = PomodoroTrackerConfig.pomodoroTracker()
    }

    def "Should save a pomodoro session"() {
        given:
        def pomodoroSession = defaultSessionBuilder().withOwnerId(ownerId).build()

        when:
        pomodoroTracker.saveSession(pomodoroSession)

        then:
        pomodoroTracker.allUserSessions(ownerId).contains(pomodoroSession)
    }

    def "Should return a sessions with given category"() {
        given:
        def booksPomodoroSession = todayBookSession()
        def codingPomodoroSession = todayCodingSession()

        when:
        pomodoroTracker.saveSession(booksPomodoroSession)
        pomodoroTracker.saveSession(codingPomodoroSession)
        def booksCategory = CATEGORY_BOOK
        def sessionsByCategory = pomodoroTracker.sessionsByCategory(booksCategory.getCategoryName())

        then:
        sessionsByCategory.size() == 1
        sessionsByCategory.first().getCategory() == booksCategory.categoryName
    }

    def "Should count sessions by category"() {
        given:
        def sessions = [todayBookSession(), todayBookSession(), todayCodingSession()]

        when:
        pomodoroTracker.saveSessions(sessions)

        then:
        pomodoroTracker.countSessionsByCategory(CATEGORY_BOOK.categoryName) == 2
        pomodoroTracker.countSessionsByCategory(CATEGORY_CODING.categoryName) == 1
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
        pomodoroTracker.countSessionsByDateAndCategory(day, category.categoryName) == expectedNumberOfSessions

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
        pomodoroTracker.createCategory(new PomodoroCategory(CATEGORY_BOOK.categoryName, new DailyGoal(2), new WeeklyGoal(5)))
        pomodoroTracker.createCategory(new PomodoroCategory(CATEGORY_CODING.categoryName, new DailyGoal(2), new WeeklyGoal(5)))

        expect:
        pomodoroTracker.dailyPomodoroGoalForCategoryFinished(CATEGORY_BOOK.categoryName, LocalDate.now()) == dailyBookGoalfinished
        pomodoroTracker.dailyPomodoroGoalForCategoryFinished(CATEGORY_CODING.categoryName, LocalDate.now()) == dailyCodingGoalFinished

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
    def "Should get sessions from user"() {
        given:
        def userId = ownerId
        def sessions = [
                defaultSessionBuilder().withOwnerId(userId).build(),
                defaultSessionBuilder().withOwnerId(userId).build(),
                defaultSessionBuilder().withOwnerId(userId).build(),
                defaultSessionBuilder().withOwnerId(userId).build()
        ]
        sessions.addAll(sessionCollectionOf(5))
        pomodoroTracker.saveSessions(sessions)

        when:
        def retrievedSessions = pomodoroTracker.allUserSessions(userId)

        then:
        retrievedSessions.every { session -> (session.sessionsOwner() == userId) }
    }


    def "Should edit category weekly goal"() {
        given:
        def numberOfSessionsToFulfill = 2
        def weeklyGoal = new WeeklyGoal(numberOfSessionsToFulfill)
        def dailyGoal = new DailyGoal(numberOfSessionsToFulfill)
        def bookCategory = new PomodoroCategory("book", dailyGoal, weeklyGoal)
        pomodoroTracker.createCategory(bookCategory)
        def expectedWeeklyGoal = new WeeklyGoal(10)
        def categoryName = bookCategory.getCategoryName()
        when:
        pomodoroTracker.editWeeklyGoalForCategory(categoryName, expectedWeeklyGoal)
        then:
        def bookCategoryWeeklyGoalSessions = pomodoroTracker.weeklyGoalForCategory(categoryName)
        expectedWeeklyGoal.numberOfSessionsToFulfill == bookCategoryWeeklyGoalSessions
    }

    def "Should get number of sessions grouped by category"() {
        given:
        SessionCollectionBuilder makeSessions = new SessionCollectionBuilder()
        Collection<PomodoroSession> sessions = []
        sessions.addAll(makeSessions.withName("DDD").withCategory(CATEGORY_BOOK).getAmountOf(5))
        sessions.addAll(makeSessions.withName("My new App").withCategory(CATEGORY_CODING).getAmountOf(4))
        pomodoroTracker.saveSessions(sessions)

        when:
        def summary = pomodoroTracker.sessionsSummaryForUser(ownerId)

        then:
        summary == [Book: 5L, Coding: 4L]
    }

}
