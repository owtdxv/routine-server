package com.codruwh.routine.common;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
  private final HttpStatus status;

  // 상태코드를 동적으로 받는 예외 객체
  public ApiException( com.codruwh.routine.common.HttpStatus status, String message) {
    super(message); // RuntimeException의 메세지로 전달
    this.status = status;
  }
}
