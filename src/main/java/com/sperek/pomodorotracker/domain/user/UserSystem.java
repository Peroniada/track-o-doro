package com.sperek.pomodorotracker.domain.user;

import com.sperek.pomodorotracker.application.ports.secondary.UserRepository;
import com.sperek.pomodorotracker.domain.tracker.dto.UserDTO;
import com.sperek.pomodorotracker.domain.user.exceptions.LoginException;
import com.sperek.pomodorotracker.domain.user.exceptions.PasswordChangeException;
import com.sperek.pomodorotracker.domain.user.exceptions.UserAlreadyExistsException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class UserSystem {

  private UserRepository userRepository;
  private PasswordEncryptor passwordEncryptor;

  public UserSystem(UserRepository userRepository,
      PasswordEncryptor passwordEncryptor) {
    this.userRepository = userRepository;
    this.passwordEncryptor = passwordEncryptor;
  }

  public void createAccount(UserDTO newUser) {

    if (userExists(newUser)) {
      throw new UserAlreadyExistsException();
    }
    final byte[] salt = PasswordEncryptor.generateSalt();
    final String encryptedPassword = encryptPassword(newUser.getPassword(), salt);
    userRepository
        .save(new User(newUser.getUserMail(), encryptedPassword, UUID.randomUUID(),
            salt, UUID.randomUUID()));
  }

  private boolean userExists(UserDTO newUser) {
    return users().stream().anyMatch(user -> user.getEmail().equals(newUser.getUserMail()));
  }

  public Collection<User> users() {
    return userRepository.findAll();
  }

  public User userWithId(UUID userId) {
    return userRepository.getOne(userId);
  }

  public void changePassword(UUID userId, String oldPassword, String newPassword) {
    User user = validateNonNull(userWithId(userId));
    if (passwordMatchesUser(oldPassword, user)) {
      final byte[] salt = user.getSalt();
      final String encryptedPassword = encryptPassword(newPassword, salt);
      userRepository.save(new User(user.getEmail(), encryptedPassword, userId, salt,
          user.getUserGoals().getUserGoalsId()));
    } else {
      throw new PasswordChangeException();
    }
  }

  public User login(String userMail, String password) throws LoginException {
    final User user = validateNonNull(userRepository.findByMail(userMail));
    if (passwordMatchesUser(password, user)) {
      return user;
    }
    throw new LoginException();
  }

  private boolean passwordMatchesUser(String password, User user) {
    return user.getPassword().equals(encryptPassword(password, user.getSalt()));
  }

  private User validateNonNull(User user) {
    return Optional.ofNullable(user)
        .orElseThrow(LoginException::new);
  }

  private String encryptPassword(String password, byte[] salt) {
    return passwordEncryptor.encrypt(password, salt);
  }
}