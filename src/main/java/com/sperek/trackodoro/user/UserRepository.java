package com.sperek.trackodoro.user;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

  void save(User newUser);

  Collection<User> findAll();

  User getOne(UUID userId);

  User findByMail(String userMail);
}
