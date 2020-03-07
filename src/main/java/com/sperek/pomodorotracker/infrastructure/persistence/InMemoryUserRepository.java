package com.sperek.pomodorotracker.infrastructure.persistence;

import com.sperek.pomodorotracker.application.ports.secondary.UserRepository;
import com.sperek.pomodorotracker.domain.user.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryUserRepository implements UserRepository {

  private Map<UUID, User> users;

  public InMemoryUserRepository() {
    this.users = new HashMap<>();
  }

  @Override
  public void save(User newUser) {
    this.users.put(newUser.getId(), newUser);
  }

  @Override
  public Collection<User> findAll() {
    return this.users.values();
  }

  @Override
  public Optional<User> getOne(UUID userId) {
    return Optional.ofNullable(this.users.get(userId));
  }

  @Override
  public Optional<User> findByMail(String userMail) {
    return users.values().stream().filter(user -> user.getEmail().equals(userMail)).findFirst();
  }
}
