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
  private String host;

  public DSLContext dsl(Connection conn) {
    return DSL.using(conn, SQLDialect.POSTGRES);
  }

  public Connection connection() throws SQLException {
    return DriverManager.getConnection("jdbc:postgresql://" + host, userName, password);
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }
}
