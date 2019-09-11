package com.sperek.trackodoro.user;

import com.sperek.trackodoro.user.exceptions.LoginException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryUserRepository implements UserRepository {

  private Map<UUID, User> users;

  public InMemoryUserRepository() {
    this.users = new HashMap<>();
  }

  @Override
  public void save(User newUser) {
    this.users.put(newUser.getUserId(), newUser);
  }

  @Override
  public Collection<User> findAll() {
    return this.users.values();
  }

  @Override
  public User getOne(UUID userId) {
    return this.users.get(userId);
  }

  @Override
  public User findByMail(String userMail) {
    return users.values().stream().filter(user -> user.getUserMail().equals(userMail)).findFirst()
        .orElseThrow(
            LoginException::new);
  }
}
