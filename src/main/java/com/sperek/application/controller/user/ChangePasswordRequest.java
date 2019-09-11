package com.sperek.application.controller.user;

import lombok.Data;

@Data
class ChangePasswordRequest {

  private String oldPassword;
  private String newPassword;
}
