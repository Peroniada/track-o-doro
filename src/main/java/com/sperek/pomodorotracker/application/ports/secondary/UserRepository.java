package com.sperek.pomodorotracker.application.ports.secondary;

import com.sperek.pomodorotracker.domain.user.User;
import java.util.Collection;
import java.util.UUID;

public interface UserRepository {

  void save(User newUser);

  Collection<User> findAll();

  User getOne(UUID userId);

  User findByMail(String userMail);
}
