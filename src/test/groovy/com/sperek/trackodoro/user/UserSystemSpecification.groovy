package com.sperek.trackodoro.user

import com.sperek.trackodoro.user.exceptions.LoginException
import com.sperek.trackodoro.user.exceptions.UserAlreadyExistsException
import spock.lang.Specification

class UserSystemSpecification extends Specification {

    private UserSystem userSystem
    private PBKDF2PasswordEncryptor passwordEncryptor

    def setup() {
        setup:
        passwordEncryptor = new PBKDF2PasswordEncryptor()
        def repository = new InMemoryUserRepository()
        userSystem = new UserSystem(repository, passwordEncryptor)
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
        def user = new User(userMail, oldPassword, userId, PasswordEncryptor.generateSalt())
        userSystem.createAccount(user)

        when: "User request for password change"
        def newPassword = "newPassword"
        userSystem.changePassword(userId, newPassword)

        then: "User password is changed"
        def retrievedUser = userSystem.userWithId(userId)
        retrievedUser.getPassword() == passwordEncryptor.encrypt(newPassword, retrievedUser.getSalt())
    }

    def "Should log in"() {
        given: "A new user"
        def mail = "mail@mail.com"
        def password = "password"
        def uuid = UUID.randomUUID()
        def user = new User(mail, password, uuid)
        userSystem.createAccount(user)

        when: "User tries to login"
        userSystem.login(mail, password)

        then: "User had been authenticated"
    }

    def "Non existing user cannot log in"() {
        when: "Non existing user tries to log in"
        userSystem.login("nonExisting@mail.com", "password")

        then: "Exception is thrown"
        def ex = thrown(LoginException)
        ex.message == "Invalid mail or password"
    }

    def "User cannot login while providing wrong password"() {
        given: "A new user"
        def mail = "mail@mail.com"
        def password = "password"
        def uuid = UUID.randomUUID()
        def user = new User(mail, password, uuid, PasswordEncryptor.generateSalt())
        userSystem.createAccount(user)

        when: "A user tries to log in with wrong password"
        userSystem.login(mail, "wrongPassword")

        then: "Exception is thrown"
        def ex = thrown(LoginException)
        ex.message == "Invalid mail or password"
    }

    def "Should set daily goal for user"() {}
    def "Should set weekly goal for user"() {}
}
