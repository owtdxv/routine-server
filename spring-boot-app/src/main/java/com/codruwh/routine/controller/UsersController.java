package com.codruwh.routine.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codruwh.routine.application.UsersService;
import com.codruwh.routine.controller.dto.ProfileResponseDto;
import com.codruwh.routine.domain.Profile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 사용자와 관련된 API들을 정의하는 컨트롤러 입니다
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "사용자 관련 API")
@RequiredArgsConstructor
public class UsersController {

  private final UsersService usersService;

  @Operation(
    summary = "사용자 정보 조회",
    description = "현재 로그인한 사용자의 프로필 정보를 조회합니다. AccessToken이 필요합니다"
  )
  @SecurityRequirement(name = "bearerAuth")
  @GetMapping("/me")
  public ResponseEntity<ProfileResponseDto> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
    // JwtAuthenticationFilter에서 UserDetails의 username에 UID가 들어갑니다
    UUID userId = UUID.fromString(userDetails.getUsername());
    Profile profile = usersService.getProfileByUserId(userId);

    return ResponseEntity.ok(new ProfileResponseDto(profile));
  }
}
