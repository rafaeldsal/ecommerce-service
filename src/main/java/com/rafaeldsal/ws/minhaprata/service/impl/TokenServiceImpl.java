package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.model.jpa.UserCredentials;
import com.rafaeldsal.ws.minhaprata.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

  @Value("${webservices.minhaprata.jwt.expiration}")
  private String expiration;

  @Value("${webservices.minhaprata.jwt.secret}")
  private String secret;

  @Override
  public String getToken(Authentication auth) {

    UserCredentials userCredentials = (UserCredentials) auth.getPrincipal();
    Date today = new Date();
    Date expirationDate = new Date(today.getTime() + Long.parseLong(expiration));

    return Jwts.builder()
        .issuer("API Minha Prata")
        .subject(userCredentials.getId())
        .issuedAt(today)
        .expiration(expirationDate)
        .signWith(getKey(), Jwts.SIG.HS256)
        .compact();
  }

  public Boolean isValid(String token) {
    try {
      getClaimsJws(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  @Override
  public String getUserId(String token) {
    Jws<Claims> claimsJws = getClaimsJws(token);
    return claimsJws.getPayload().getSubject();
  }

  private Jws<Claims> getClaimsJws(String token) {
    return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token);
  }

  private SecretKey getKey() {
    return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
  }
}
