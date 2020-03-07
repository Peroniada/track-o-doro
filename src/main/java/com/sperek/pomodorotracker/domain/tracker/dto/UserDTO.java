package com.sperek.pomodorotracker.domain.tracker.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
  private String userMail;
  private String password;

  public UserDTO(String userMail, String password) {
    this.userMail = userMail;
    this.password = password;
  }
}
