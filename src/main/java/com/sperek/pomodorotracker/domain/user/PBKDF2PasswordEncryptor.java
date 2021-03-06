package com.sperek.pomodorotracker.domain.user;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PBKDF2PasswordEncryptor implements PasswordEncryptor {

  private static final int ITERATION_COUNT = 65536;
  private static final int KEY_LENGTH = 128;

  @Override
  public String encrypt(String password, byte[] salt) {
    try {
      KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      return new String(factory.generateSecret(spec).getEncoded(), StandardCharsets.UTF_8);
    } catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException("Password encryption failed");
    }
  }

}
