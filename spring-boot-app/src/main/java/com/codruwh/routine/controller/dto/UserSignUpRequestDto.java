package com.codruwh.routine.controller.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequestDto {
  private String email;
  private String password;
}
