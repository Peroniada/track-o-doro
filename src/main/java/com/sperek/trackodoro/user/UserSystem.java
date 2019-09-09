package com.sperek.trackodoro.user;

import java.util.Collection;
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

  public void changePassword(UUID userId, String newPassword) {
    User user = userWithId(userId);
    final byte[] salt = user.getSalt();
    userRepository.save(new User(user.getUserMail(), encryptPassword(newPassword, salt), userId, salt));
  }

  public void login(String userMail, String password) {
    final User user = userRepository.findByMail(userMail).orElseThrow(LoginException::new);
    if (!user.getPassword().equals(encryptPassword(password, user.getSalt()))) {
      throw new LoginException();
    }
  }

  private String encryptPassword(String password, byte[] salt) {
    return passwordEncryptor.encrypt(password, salt);
  }
}
