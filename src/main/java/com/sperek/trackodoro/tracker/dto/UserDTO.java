package com.sperek.trackodoro.tracker.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
  private String userMail;
  private String password;
}
