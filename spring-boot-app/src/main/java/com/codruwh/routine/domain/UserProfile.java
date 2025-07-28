package com.codruwh.routine.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="users_profile")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

  @Id
  @Column(length = 255)
  private String uid;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(length = 10)
  private String gender;

  @Builder.Default
  @Column(nullable = false)
  private int level = 1; // 기본값 1

  @Builder.Default
  @Column(nullable = false)
  private Integer lux=0;

  @Builder.Default
  @Column(nullable = false)
  private boolean isFirstLogin = false;

  @Column(length = 100)
  private String name;

  private LocalDate birthDate;

  @Column(length = 255)
  private String email;

  @PrePersist
  public void prePersist() {
    if(this.createdAt == null) {
      // UTC+9 기준 현재 시간으로 세팅
      this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }
  }
}
