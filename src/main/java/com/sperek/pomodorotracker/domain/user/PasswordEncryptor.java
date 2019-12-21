package com.sperek.pomodorotracker.domain.user;

import java.security.SecureRandom;

public interface PasswordEncryptor {

  String encrypt(String password, byte[] salt);

  static byte[] generateSalt() {
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    return salt;
  }

}
