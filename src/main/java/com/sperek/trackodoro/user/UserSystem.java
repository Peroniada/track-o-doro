package com.sperek.trackodoro.user;

import java.util.Collection;
import java.util.UUID;

public class UserSystem {

  private UserRepository userRepository;

  public UserSystem(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void createAccount(User newUser) {
    if(userExists(newUser)) {
      throw new UserAlreadyExistsException();
    }
    userRepository.save(newUser);
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
    User oldUser = userWithId(userId);
    userRepository.save(new User(oldUser.getUserMail(), newPassword, userId));
  }
}
