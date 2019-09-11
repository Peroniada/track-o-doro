package com.sperek.application.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.security.SignatureException;

public interface Tokenizer {
  String generate(String userId, String role);
  Jws<Claims> parser(String token) throws SignatureException;
}
