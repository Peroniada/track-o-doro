package com.sperek.pomodorotracker.application.api.requests;

import lombok.Data;

@Data
public
class ChangePasswordRequest {

  private String oldPassword;
  private String newPassword;
}
