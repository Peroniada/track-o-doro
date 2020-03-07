package com.sperek.pomodorotracker.domain.user;

import com.sperek.pomodorotracker.application.ports.secondary.UserRepository;
import com.sperek.pomodorotracker.domain.tracker.dto.UserDTO;
import com.sperek.pomodorotracker.domain.user.exceptions.LoginException;
import com.sperek.pomodorotracker.domain.user.exceptions.PasswordChangeException;
import com.sperek.pomodorotracker.domain.user.exceptions.UserAlreadyExistsException;
import java.util.Collection;
import java.util.UUID;

public class UserService {

  private UserRepository userRepository;
  private PasswordEncryptor passwordEncryptor;

  public UserService(UserRepository userRepository,
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
        .save(new User(UUID.randomUUID(), newUser.getUserMail(), encryptedPassword,
            salt, UUID.randomUUID()));
  }

  private boolean userExists(UserDTO newUser) {
    return userRepository.findByMail(newUser.getUserMail()).map(User::getEmail).orElse("")
        .equals(newUser.getUserMail());
  }

  public Collection<User> users() {
    return userRepository.findAll();
  }

  public User userWithId(UUID userId) {
    return userRepository.getOne(userId).orElseThrow(UserNotFoundException::new);
  }

  public void changePassword(UUID userId, String oldPassword, String newPassword) {
    final User user = userWithId(userId);
    if (passwordMatchesUser(oldPassword, user)) {
      final byte[] salt = user.getSalt();
      final String encryptedPassword = encryptPassword(newPassword, salt);
      userRepository.save(new User(userId, user.getEmail(), encryptedPassword, salt,
          user.getUserGoals()));
    } else {
      throw new PasswordChangeException();
    }
  }

  public User login(String userMail, String password) {
    final User user = userRepository.findByMail(userMail).orElseThrow(LoginException::new);
    if (passwordMatchesUser(password, user)) {
      return user;
    }
    throw new LoginException();
  }

  private boolean passwordMatchesUser(String password, User user) {
    return user.getPassword().equals(encryptPassword(password, user.getSalt()));
  }

  private String encryptPassword(String password, byte[] salt) {
    return passwordEncryptor.encrypt(password, salt);
  }

  public User userWithEmail(String userMail) {
    return userRepository.findByMail(userMail).orElseThrow(UserNotFoundException::new);
  }
}
