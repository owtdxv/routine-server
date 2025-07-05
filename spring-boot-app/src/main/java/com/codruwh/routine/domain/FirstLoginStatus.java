package com.codruwh.routine.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "first_login_status")
@Getter
@Setter
public class FirstLoginStatus {

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

  /**
   * 최초 로그인 여부
   */
  @Column(name = "first_login", nullable = false)
  private boolean firstLogin = true;
}
