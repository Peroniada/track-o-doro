import com.sperek.trackodoro.user.InMemoryUserRepository
import com.sperek.trackodoro.user.User
import com.sperek.trackodoro.user.UserAlreadyExistsException
import com.sperek.trackodoro.user.UserSystem
import spock.lang.Specification

class UserSystemSpecification extends Specification {

    private UserSystem userSystem

    def setup() {
        setup:
        userSystem = new UserSystem(new InMemoryUserRepository());
    }

    def "Should create a user account"() {
        given: "A user credentials"
        def userEmail = "mail@mail.com"
        def password = "password"
        def userId = UUID.randomUUID()

        when: "User request for creating and account"
        userSystem.createAccount(new User(userEmail, password, userId))

        then: "User is created"
        userSystem.users().size() > 0
        userSystem.userWithId(userId).getUserMail() == userEmail
    }

    def "Should not be able to create a user with the same mail"() {
        given: "A user credentials"
        def userMail = "mail@mail.com"
        def password = "password"

        when: "User tries to create two accounts with the same mail"
        userSystem.createAccount(new User(userMail, password, UUID.randomUUID()))
        userSystem.createAccount(new User(userMail, password, UUID.randomUUID()))

        then: "One account remains, Exception is thrown"
        userSystem.users().size() == 1
        def ex = thrown(UserAlreadyExistsException)
        ex.message == 'User with registered email already exists.'
    }

    def "Should be able to change a password"() {
        given: "A user account"
        def userMail = "mail@mail.com"
        def oldPassword = "password"
        def userId = UUID.randomUUID()
        def user = new User(userMail, oldPassword, userId)
        userSystem.createAccount(user);

        when: "User request for password change"
        def newPassword = "newPassword"
        userSystem.changePassword(userId, newPassword)

        then: "User password is changed"
        userSystem.userWithId(userId).password == newPassword
    }

    def "Should set daily goal for user"() {}
    def "Should set weekly goal for user"() {}
}
