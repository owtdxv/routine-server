package com.codruwh.routine.controller.dto;

import java.util.UUID;

public class FirstLoginStatusDto {
  private UUID userId;
  private boolean firstLogin;

  public FirstLoginStatusDto(UUID userId, boolean firstLogin) {
      this.userId = userId;
      this.firstLogin = firstLogin;
  }

  public UUID getUserId() {
      return userId;
  }

  public boolean isFirstLogin() {
      return firstLogin;
  }
}


