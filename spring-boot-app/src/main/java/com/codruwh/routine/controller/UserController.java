package com.codruwh.routine.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codruwh.routine.application.UserService;
import com.codruwh.routine.common.ApiException;
import com.codruwh.routine.controller.dto.UserProfileResponseDto;
import com.codruwh.routine.controller.dto.UserSettingResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @Operation(
        summary = "사용자 프로필 조회",
        description = "현재 로그인한 사용자의 프로필 정보를 조회합니다. AccessToken이 필요합니다."
    )
  @SecurityRequirement(name = "bearerAuth")
  @GetMapping("/profile/{uid}")
  public ResponseEntity<UserProfileResponseDto> getUserProfile(
    @PathVariable("uid") UUID uid,
    @AuthenticationPrincipal UserDetails userDetails
  ) {
    UUID currentUserId = UUID.fromString(userDetails.getUsername());

    System.out.println(currentUserId);

    if(!currentUserId.equals(uid)) {
      throw new ApiException(HttpStatus.FORBIDDEN, "본인 외 사용자의 정보는 열람할 수 없습니다");
    }

    UserProfileResponseDto dto = userService.getUserProfileById(uid);
    return ResponseEntity.ok(dto);
  }

  @Operation(
        summary = "사용자 설정값 조회",
        description = "현재 로그인한 사용자의 설정값 정보를 조회합니다. AccessToken이 필요합니다."
    )
  @SecurityRequirement(name = "bearerAuth")
  @GetMapping("/settings/{uid}")
  public ResponseEntity<UserSettingResponseDto> getUserSetting(
    @PathVariable("uid") UUID uid,
    @AuthenticationPrincipal UserDetails userDetails
  ) {
    UUID currentUserId = UUID.fromString(userDetails.getUsername());

    System.out.println(currentUserId);

    if(!currentUserId.equals(uid)) {
      throw new ApiException(HttpStatus.FORBIDDEN, "본인 외 사용자의 정보는 열람할 수 없습니다");
    }

    UserSettingResponseDto dto = userService.getUserSettingById(uid);
    return ResponseEntity.ok(dto);
  }
}
