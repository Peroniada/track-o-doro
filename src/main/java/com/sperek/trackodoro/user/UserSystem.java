package com.sperek.trackodoro.user;

import com.sperek.trackodoro.user.exceptions.LoginException;
import com.sperek.trackodoro.user.exceptions.PasswordChangeException;
import com.sperek.trackodoro.user.exceptions.UserAlreadyExistsException;
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

  public void createAccount(User newUser) {
    if (userExists(newUser)) {
      throw new UserAlreadyExistsException();
    }
    final byte[] salt = PasswordEncryptor.generateSalt();
    final String encryptedPassword = encryptPassword(newUser.getPassword(), salt);
    userRepository
        .save(new User(newUser.getUserMail(), encryptedPassword, newUser.getUserId(), salt));
  }

  private boolean userExists(User newUser) {
    return users().stream().anyMatch(user -> user.getUserMail().equals(newUser.getUserMail()));
  }

  public Collection<User> users() {
    return userRepository.findAll();
  }

  public User userWithId(UUID userId) {
    return userRepository.getOne(userId);
  }

  public void changePassword(UUID userId, String oldPassword, String newPassword) {
    User user = validateNonNull(userWithId(userId));
    if(passwordsEqual(oldPassword, user)) {
      final byte[] salt = user.getSalt();
      final String encryptedPassword = encryptPassword(newPassword, salt);
      userRepository.save(new User(user.getUserMail(), encryptedPassword, userId, salt));
    }
    throw new PasswordChangeException();
  }

  public User login(String userMail, String password) throws LoginException {
    final User user = validateNonNull(userRepository.findByMail(userMail));
    if (passwordsEqual(password, user)) {
      return user;
    }
    throw new LoginException();
  }

  private boolean passwordsEqual(String password, User user) {
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
