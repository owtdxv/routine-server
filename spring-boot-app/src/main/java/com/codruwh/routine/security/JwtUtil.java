package com.codruwh.routine.security;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

  @Value("${supabase.jwt.secret}")
  private String secret;

  private Key key;

  @PostConstruct
  public void init() {
    // JWT Secret을 Key객체로 변환
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
  }

  /**
   * 토큰에서 모든 Claim을 추출합니다
   * @param token JWT
   * @return Claims 객체
   */
  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder()
    .setSigningKey(key)
    .build()
    .parseClaimsJws(token)
    .getBody();
  }

  /**
   * 토큰에서 사용자 UID(sub)를 추출합ㄴ다
   * @param token JWT
   * @return 사용자 UUID
   */
  public String getUidFromToken(String token) {
    return getAllClaimsFromToken(token).getSubject();
  }

  /**
   * 토큰의 유효성을 검사합니다
   * @param token JWT
   * @return 유효시 true
   */
  public Boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
