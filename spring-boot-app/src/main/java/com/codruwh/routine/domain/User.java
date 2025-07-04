package com.codruwh.routine.domain;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Supabase의 auth.users에 매핑되는 엔티티입니다
 * Profiles와의 관계 설정을 위해 생성합니다
 */
@Entity
@Table(name = "users", schema = "auth")
@Getter
@Setter
public class User {

  @Id
  private UUID id;

  /**
   * 다른 정보도 필요하다면 넣을 수 있으나,
   * 적어도 id 컬럼은 꼭 들어가야 합니다
   */

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnore // 이게 없으면 User또는 Profile을 참조할 때 무한루프가 생깁니다
  private Profile profile;
}
