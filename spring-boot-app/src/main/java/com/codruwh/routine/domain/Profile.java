package com.codruwh.routine.domain;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "profiles")
@Getter
@Setter
public class Profile {

  /**
   * Primary Key이자 auth.users 테이블을 참조하는 외래 키 입니다
   * @MapsId를 사용해서 User Entity의 id를 이 Entity의 Primary Key로 직접 사용하도록 매핑합니다
   */
  @Id
  private UUID id;

  /**
   * User Entity와의 1:1관계를 정의합니다
   * 필요할 때만 조회할 수 있도록 FetchType을 LAZY로 지정합니다
   */
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "id")
  private User user;

  @Column(name = "display_name")
  private String displayName; // 닉네임

  @Column(name = "birth_date")
  private LocalDate birthDate; // 생년월일

  @Column(name = "gender")
  private String gender; // 성별

  /**
   * Supabase 자체에서 기본값이 지정되어 있으므로
   * JPA 단에서는 수정되지 않도록 합니다.
   */
  @Column(name = "created_at", nullable = false, updatable = false)
  @CreationTimestamp
  private OffsetDateTime createdAt; // 생성 시각

  @Column(name = "avatar", columnDefinition = "TEXT")
  private String avatar; // 프로필 이미지 url

  @Column(name = "title")
  private String title; // 칭호

  @Column(name = "height")
  private Float height; // 키

  @Column(name = "weight")
  private Float weight; // 몸무게

  @Column(name = "email")
  private String email; // 이메일
}
