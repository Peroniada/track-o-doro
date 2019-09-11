package com.sperek.application.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

public class JWTTokenizer implements Tokenizer {

  private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  @Override
  public String generate(String userId, String role) {
    final Date tokenExpirationDate = Date.from(ZonedDateTime.now().plusMinutes(10L).toInstant());
    String jws = Jwts.builder()
        .setSubject(userId)
        .claim("role", role)
        .setExpiration(tokenExpirationDate)
        .signWith(key)
        .compact();
    assert Jwts.parser().setSigningKey(key).parseClaimsJws(jws).getBody().getSubject().equals(userId);
    assert Jwts.parser().setSigningKey(key).parseClaimsJws(jws).getBody().get("role").equals(role);

    return jws;
  }

  @Override
  public Jws<Claims> parser(String token) throws SignatureException {
    return Jwts.parser().setSigningKey(key).parseClaimsJws(token);
  }
}
