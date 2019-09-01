package com.sperek.trackodoro.user;

import java.util.Collection;
import java.util.UUID;

public interface UserRepository {

  void save(User newUser);

  Collection<User> findAll();

  User getOne(UUID userId);
}
