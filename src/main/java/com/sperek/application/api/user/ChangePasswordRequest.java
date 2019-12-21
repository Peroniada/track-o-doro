package com.sperek.application.api.user;

import lombok.Data;

@Data
class ChangePasswordRequest {

  private String oldPassword;
  private String newPassword;
}
