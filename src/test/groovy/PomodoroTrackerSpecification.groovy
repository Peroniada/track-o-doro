import com.sperek.trackodoro.category.PomodoroCategory
import com.sperek.trackodoro.goal.DailyGoal
import com.sperek.trackodoro.goal.WeeklyGoal
import com.sperek.trackodoro.sessionFilter.CategorySpecification
import com.sperek.trackodoro.sessionFilter.DateSpecification
import com.sperek.trackodoro.tracker.PomodoroTracker
import com.sperek.trackodoro.tracker.PomodoroTrackerConfig
import com.sperek.trackodoro.tracker.session.PomodoroSession
import spock.lang.Specification

import java.time.LocalDate

import static SessionsDSL.*
import static SessionsDSL.CATEGORY_BOOK

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
        def specification = new CategorySpecification(booksCategory)
        def sessionsByCategory = pomodoroTracker.findSatisfyingSessions(specification)

        then:
        sessionsByCategory.size() == 1
        sessionsByCategory.first().sessionCategoryName() == booksCategory
    }

    def "Should count sessions by category"() {
        given:
        def sessions = [todayBookSession(), todayBookSession(), todayCodingSession()]

        when:
        pomodoroTracker.saveSessions(sessions)

        then:
        def bookCategorySpecification = new CategorySpecification(CATEGORY_BOOK)
        def codingCategorySpecification = new CategorySpecification(CATEGORY_CODING)
        pomodoroTracker.countSatisfyingSessions(bookCategorySpecification) == 2
        pomodoroTracker.countSatisfyingSessions(codingCategorySpecification) == 1
    }

    def "Should count sessions by date"() {
        given:
        def sessions = [yesterdayBookSession(), todayBookSession(), yesterdayCodingSession()]

        when:
        pomodoroTracker.saveSessions(sessions)

        then:
        def yesterday = yesterdayDateTime().toLocalDate()
        def today = todayDateTime().toLocalDate()
        def yesterdayDateSpecification = new DateSpecification(yesterday)
        def todayDateSpecification = new DateSpecification(today)
        pomodoroTracker.countSatisfyingSessions(yesterdayDateSpecification) == 2
        pomodoroTracker.countSatisfyingSessions(todayDateSpecification) == 1
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
        def specification = new DateSpecification(day) & (new CategorySpecification(category))
        pomodoroTracker.countSatisfyingSessions(specification) == expectedNumberOfSessions

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

    def 'Should achieve daily goal for category'() {
        setup:
        pomodoroTracker.saveSessions(sessions)
        pomodoroTracker.createCategory(new PomodoroCategory(CATEGORY_BOOK, new DailyGoal(2), new WeeklyGoal(5), ownerId))
        pomodoroTracker.createCategory(new PomodoroCategory(CATEGORY_CODING, new DailyGoal(2), new WeeklyGoal(5), ownerId))

        expect:
        pomodoroTracker.dailyPomodoroGoalForCategoryFinished(ownerId, CATEGORY_BOOK, LocalDate.now()) == dailyBookGoalfinished
        pomodoroTracker.dailyPomodoroGoalForCategoryFinished(ownerId, CATEGORY_CODING, LocalDate.now()) == dailyCodingGoalFinished

        where:
        sessions                      || dailyBookGoalfinished || dailyCodingGoalFinished
        booksFinishedCodingFailed()   || true                  || false
        booksFailedCodingFinished()   || false                 || true
        booksFinishedCodingFinished() || true                  || true
        booksFailedCodingFailed()     || false                 || false

    }

    def 'Should achieve weekly goal for category'() {
        setup:
        pomodoroTracker.saveSessions(sessions)
        pomodoroTracker.createCategory(new PomodoroCategory(CATEGORY_BOOK, new DailyGoal(1), new WeeklyGoal(5), ownerId))

        expect:
        pomodoroTracker.weeklyPomodoroGoalForCategoryFinished(ownerId, CATEGORY_BOOK, LocalDate.now()) == weeklyGoalFinished

        where:
        sessions                 || weeklyGoalFinished
        threeCompletedThisWeek() || false
        sevenCompletedThisWeek() || true
    }

    def "Should achieve weekly goal of pomodoros"() {
        setup:
        pomodoroTracker.saveSessions(sessions)
        pomodoroTracker.editWeeklyGoal(new WeeklyGoal(5))

        expect:
        pomodoroTracker.weeklyGoalFinishedForDate(LocalDate.now()) == weeklyGoalFinished

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
        pomodoroTracker.createCategory(new PomodoroCategory("book", new DailyGoal(1), new WeeklyGoal(numberOfSessionsToFulfill), ownerId))

        then:
        def categoriesCreatedByUser = pomodoroTracker.categoriesCreatedByUser(ownerId) as List<PomodoroCategory>
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
        def bookCategory = new PomodoroCategory("book", dailyGoal, weeklyGoal, ownerId)
        pomodoroTracker.createCategory(bookCategory)
        def expectedWeeklyGoal = new WeeklyGoal(10)
        def categoryName = bookCategory.getCategoryName()
        when:
        pomodoroTracker.editWeeklyGoalForCategory(ownerId, categoryName, expectedWeeklyGoal)
        then:
        def bookCategoryWeeklyGoalSessions = pomodoroTracker.weeklyGoalForCategory(ownerId, categoryName)
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

    def "Should return the session with given id"() {
        given:
        def sessionId = UUID.randomUUID()
        PomodoroSession session = bookSessionBuilder().withId(sessionId).build()
        pomodoroTracker.saveSessions(sessionCollectionOf(5))
        pomodoroTracker.saveSession(session)
        when:
        def actualSession = pomodoroTracker.getSession(sessionId)
        then:
        actualSession.getId() == sessionId
    }
}
