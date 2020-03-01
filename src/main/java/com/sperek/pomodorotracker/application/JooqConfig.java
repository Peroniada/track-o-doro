package com.sperek.pomodorotracker.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class JooqConfig {

  private String userName;
  private String password;
  private String url;

  public JooqConfig(String url, String userName, String password) {
    this.userName = userName;
    this.password = password;
    this.url = url;
  }

  public DSLContext dsl() {
//    userName = "postgres";
//    password = "root";
//    url = "jdbc:postgresql://localhost:5432/pomodoro-tracker";

    try (Connection conn = DriverManager.getConnection(url, userName, password)) {
      return DSL.using(conn, SQLDialect.POSTGRES);
    }
    catch (SQLException e) {
      throw new RuntimeException(e.getCause());
    }
  }
}