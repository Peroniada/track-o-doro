package com.sperek.pomodorotracker.infrastructure.persistence;

import static com.sperek.infrastructure.Tables.USER_ACCOUNT;

import com.sperek.infrastructure.enums.Role;
import com.sperek.pomodorotracker.application.JooqConfig;
import com.sperek.pomodorotracker.application.ports.secondary.UserRepository;
import com.sperek.pomodorotracker.domain.user.User;
import com.sperek.pomodorotracker.domain.user.UserRole;
import java.util.Collection;
import java.util.UUID;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;

public class JooqUserRepository implements UserRepository {

  private JooqConfig jooqConfig;

  public JooqUserRepository(JooqConfig jooqConfig) {
    this.jooqConfig = jooqConfig;
  }

  @Override
  public void save(User newUser) {
    final UserRole userRole = newUser.getUserRole();
    Role role = Role.valueOf(userRole.name());
    final DSLContext dsl = dsl();
    dsl.insertInto(USER_ACCOUNT)
        .columns(USER_ACCOUNT.PK_USER_ID, USER_ACCOUNT.EMAIL, USER_ACCOUNT.PASSWORD,
            USER_ACCOUNT.ROLE, USER_ACCOUNT.SALT, USER_ACCOUNT.FK_USER_GOALS_ID)
        .values(newUser.getId(), newUser.getEmail(), newUser.getPassword(), role, newUser.getSalt(),
            newUser.getUserGoals().getUserGoalsId()).execute();
  }

  @Override
  public Collection<User> findAll() {
    return selectFromUserAccount().fetchInto(User.class);
  }

  private SelectJoinStep<Record> selectFromUserAccount() {
    return dsl().select().from(USER_ACCOUNT);
  }

  @Override
  public User getOne(UUID userId) {
    return selectFromUserAccount().where(USER_ACCOUNT.PK_USER_ID.eq(userId)).fetchInto(User.class).get(0);
  }

  @Override
  public User findByMail(String userMail) {
    return null;
  }

  private DSLContext dsl() {
    return this.jooqConfig.dsl();
  }
}