package com.sperek.pomodorotracker.infrastructure.persistence;

import com.sperek.infrastructure.enums.Role;
import com.sperek.infrastructure.tables.records.UserAccountRecord;
import com.sperek.pomodorotracker.application.ports.secondary.UserRepository;
import com.sperek.pomodorotracker.domain.user.User;
import com.sperek.pomodorotracker.domain.user.UserRole;
import com.sperek.pomodorotracker.infrastructure.configuration.DatabaseConfig;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectWhereStep;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static com.sperek.infrastructure.Tables.USER_ACCOUNT;
import static com.sperek.infrastructure.Tables.USER_GOALS;

public class JooqUserRepository implements UserRepository {

  private DatabaseConfig databaseConfig;

  public JooqUserRepository(DatabaseConfig databaseConfig) {
    this.databaseConfig = databaseConfig;
  }

  @Override
  public void save(User newUser) {
    final UserRole userRole = newUser.getUserRole();
    Role role = Role.valueOf(userRole.name());
    try (Connection conn = databaseConfig.connection()) {
      DSLContext dsl = databaseConfig.dsl(conn);
      dsl.insertInto(USER_GOALS)
          .columns(USER_GOALS.PK_USER_GOALS_ID, USER_GOALS.DAILY_GOAL, USER_GOALS.WEEKLY_GOAL)
          .values(newUser.getUserGoals(), 1, 1)
          .execute();
      dsl.insertInto(USER_ACCOUNT)
          .columns(USER_ACCOUNT.PK_USER_ID, USER_ACCOUNT.EMAIL, USER_ACCOUNT.PASSWORD,
              USER_ACCOUNT.ROLE, USER_ACCOUNT.SALT, USER_ACCOUNT.FK_USER_GOALS_ID)
          .values(newUser.getId(), newUser.getEmail(), newUser.getPassword(), role,
              newUser.getSalt(),
              newUser.getUserGoals()).execute();
    } catch (SQLException e) {
      throw new DBConnectionException(e.getMessage());
    }
  }

  @Override
  public Collection<User> findAll() {
    try (Connection conn = databaseConfig.connection()) {
      return selectFromUserAccount(conn).fetchInto(User.class);
    } catch (SQLException e) {
      throw new DBConnectionException(e.getMessage());
    }
  }

  private SelectWhereStep<UserAccountRecord> selectFromUserAccount(Connection connection) {
    return databaseConfig.dsl(connection).selectFrom(USER_ACCOUNT);
  }

  @Override
  public Optional<User> getOne(UUID userId) {
    try (Connection connection = databaseConfig.connection()) {
      return Optional.ofNullable(selectFromUserAccount(connection)
          .where(USER_ACCOUNT.PK_USER_ID.eq(userId)).fetchOne().into(User.class));

    } catch (SQLException e) {
      throw new DBConnectionException(e.getMessage());
    }
  }

  @Override
  public Optional<User> findByMail(String userMail) {
    try (Connection connection = databaseConfig.connection()) {
      final Record user = selectFromUserAccount(connection)
          .where(USER_ACCOUNT.EMAIL.eq(userMail))
          .fetchOne();

      return Optional.ofNullable(user).map(usr -> usr.into(User.class));
    } catch (SQLException e) {
      throw new DBConnectionException(e.getMessage());
    }
  }

}
