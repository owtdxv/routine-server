package com.codruwh.routine.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codruwh.routine.application.UserService;
import com.codruwh.routine.common.ApiException;
import com.codruwh.routine.controller.dto.EditProfileRequestDto;
import com.codruwh.routine.controller.dto.EditSettingRequestDto;
import com.codruwh.routine.controller.dto.UserProfileResponseDto;
import com.codruwh.routine.controller.dto.UserSettingResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

  @Operation(
        summary = "사용자 프로필 수정",
        description = "프로필 정보(이름, 생년월일, 성별)를 수정합니다. AccessToken이 필요합니다."
    )
  @SecurityRequirement(name = "bearerAuth")
  @PatchMapping("/profile/{uid}")
  public ResponseEntity<Void> editProfile(
    @RequestBody EditProfileRequestDto entity,
    @AuthenticationPrincipal UserDetails userDetails,
    @PathVariable("uid") UUID uid) {
      UUID currentUserId = UUID.fromString(userDetails.getUsername());

      if(!currentUserId.equals(uid)) {
        throw new ApiException(HttpStatus.FORBIDDEN, "본인 외 사용자의 정보는 열람할 수 없습니다");
      }

      // entity에 대한 유효성 검사
      if (entity.getName() == null || entity.getName().isEmpty() ||
      entity.getBirthDate() == null || entity.getGender() == null || entity.getGender().isBlank()) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "수정 값은 null일 수 없습니다.");
      }


      userService.editUserProfile(uid, entity);

      return ResponseEntity.noContent().build();
  }

  @Operation(
        summary = "사용자 설정값 수정",
        description = "사용자의 설정값을 수정합니다. AccessToken이 필요합니다."
    )
  @SecurityRequirement(name = "bearerAuth")
  @PatchMapping("/setting/{uid}")
  public ResponseEntity<Void> editUserSetting(
    @RequestBody EditSettingRequestDto entity,
    @AuthenticationPrincipal UserDetails userDetails,
    @PathVariable("uid") UUID uid) {
      UUID currentUserId = UUID.fromString(userDetails.getUsername());

      if(!currentUserId.equals(uid)) {
        throw new ApiException(HttpStatus.FORBIDDEN, "본인 외 사용자의 정보는 열람할 수 없습니다");
      }

      // entity에 대한 유효성 검사
      if (entity.getTitleId() == null ||
        entity.getBackgroundColor() == null ||
        entity.getLumiImage() == null) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "수정 값은 null일 수 없습니다.");
      }

      if(entity.getBackgroundColor() > 10 || entity.getLumiImage() > 5) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "유효하지 않은 설정값입니다");
      }

      userService.editUserSetting(uid, entity);

      return ResponseEntity.noContent().build();
  }

  @Operation(
            summary = "사용자 루틴 달성 통계 반환",
            description = "지정된 기간 동안의 사용자의 루틴별 일일 달성 기록을 반환합니다."
    )
  @SecurityRequirement(name = "bearerAuth")
  @GetMapping("/statistics/{uid}")
  public ResponseEntity<Void> getUserRoutineStatistics(
        @Parameter(description = "사용자 고유 식별자") @PathVariable("uid") UUID uid,
        @Parameter(description = "조회 시작일 (YYYY-MM-DD)") @RequestParam("startDay") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDay,
        @Parameter(description = "조회 종료일 (YYYY-MM-DD)") @RequestParam("endDay") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDay,
        @AuthenticationPrincipal UserDetails userDetails
  ) {
      // 본인의 데이터만 조회할 수 있도록 검증합니다.
      UUID currentUserId = UUID.fromString(userDetails.getUsername());
      if (!currentUserId.equals(uid)) {
          throw new ApiException(HttpStatus.FORBIDDEN, "본인의 통계만 조회할 수 있습니다.");
      }

      // 서비스 로직을 호출하여 통계 데이터를 가져옵니다.
      // StatisticsResponseDto responseDto = statisticsService.getUserRoutineStatistics(uid, startDay, endDay);

      // return ResponseEntity.ok(responseDto);
      return ResponseEntity.noContent().build();
  }
}
