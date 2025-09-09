package com.codruwh.routine.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codruwh.routine.application.ChallengeService;
import com.codruwh.routine.application.RoutineService;
import com.codruwh.routine.common.ApiException;
import com.codruwh.routine.controller.dto.AllCollectionsResponseDto;
import com.codruwh.routine.controller.dto.AllRoutinesResponseDto;
import com.codruwh.routine.controller.dto.ChallengeResponseDto;
import com.codruwh.routine.controller.dto.RoutineAddRequestDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Routine", description = "루틴 관련 API")
@RestController
@RequestMapping("/routine")
@RequiredArgsConstructor
public class RoutineController {

  /**
   * 루틴 추천/생성 시 허용된 카테고리 목록
   */
  private static final List<String> ALLOWED_CATEGORIES =
            Arrays.asList("수면", "운동", "영양소", "햇빛", "사회적유대감");

  private final RoutineService routineService;
  private final ChallengeService challengeService;

  @Operation(
            summary = "추천 루틴 생성",
            description = "하나 이상의 카테고리를 지정하면 해당 카테고리에 높은 가중치를 부여하여 10개의 루틴을 추천해 줍니다."
    )
    @GetMapping("/recommend")
    public ResponseEntity<RecommendResponseDto> getRecommendedRoutines(
            @Parameter(description = "추천받고 싶은 카테고리 이름. 여러 개 지정 가능.", required = true, example = "sleep")
            @RequestParam("category") List<String> categories
    ) {
        // 유효성 검증: 카테고리 파라미터가 비어있는지 확인합니다.
        if (categories == null || categories.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "하나 이상의 카테고리를 지정해야 합니다.");
        }

        // 유효성 검증: 요청된 각 카테고리 이름이 유효한 카테고리 인지 확인합니다
        for (String categoryName : categories) {
            if (!ALLOWED_CATEGORIES.contains(categoryName)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리 이름입니다: " + categoryName);
            }
        }

        // 서비스 로직을 호출하여 추천 루틴 데이터를 가져옵니다.
        RecommendResponseDto responseDto = routineService.getRecommendedRoutines(categories);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "모든 루틴 정보 반환",
            description = "routines 테이블에 정의된 모든 루틴에 대한 정보를 반환합니다."
    )
    @GetMapping
    public ResponseEntity<AllRoutinesResponseDto> getAllRoutines() {
        AllRoutinesResponseDto responseDto = routineService.getAllRoutines();
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "챌린지 미션 조회",
            description = "이번 달의 챌린지 미션 내용과, 오늘 날짜 기준 챌린지를 완료한 참여자 수를 반환합니다."
    )
    @GetMapping("/challenge")
    public ResponseEntity<ChallengeResponseDto> getChallengeInfo() {
        // 서비스 로직을 호출하여 챌린지 정보를 가져옵니다.
        ChallengeResponseDto responseDto = challengeService.getChallengeInfo();
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Sera의 추천 루틴 모음 반환",
            description = "routine_collections 테이블의 모든 정보와 각 컬렉션에 포함된 루틴 목록을 함께 반환합니다."
    )
    @GetMapping("/collections")
    public ResponseEntity<AllCollectionsResponseDto> getAllRoutineCollections() {
        AllCollectionsResponseDto responseDto = routineService.getAllRoutineCollections();
        return ResponseEntity.ok(responseDto);
    }
    @Operation(
            summary = "사용자 루틴 추가",
            description = "사용자가 선택한 루틴을 사용자 루틴에 추가합니다."
    )
    @PostMapping("/add/{uid}")
    public ResponseEntity<Void> addRoutineToUser(
            @Parameter(description = "사용자 UID", required = true) @PathVariable String uid,
            @RequestBody RoutineAddRequestDto request,
            @RequestHeader("Authorization") String authorization) {

        // JWT 토큰에서 사용자 정보 추출 및 검증
        // (기존에 구현된 JWT 유틸리티 사용)
        String tokenUid = extractUidFromJWT(authorization);

        // URL의 uid와 JWT 토큰의 uid가 일치하는지 확인
        if (!uid.equals(tokenUid)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        try {
            routineService.addRoutinesToUser(uid, request);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    private String extractUidFromJWT(String authorization) {
        // "Bearer " 제거
        String token = authorization.substring(7);
        // JWT 파싱 로직 (기존 구현 사용)
        // 예시: return jwtUtil.getUidFromToken(token);
        return ""; // 실제 구현 필요
    }
    @Operation(
            summary = "사용자 루틴 추가 - 직접 작성",
            description = "사용자가 직접 작성한 루틴을 users_routine 테이블에 추가합니다."
    )
    @PostMapping("/add/custom/{uid}")
    public ResponseEntity<Void> addCustomRoutineToUser(
            @Parameter(description = "사용자 UID", required = true) @PathVariable String uid,
            @RequestBody RoutineAddCustomRequestDto request,
            @RequestHeader("Authorization") String authorization) {

        // JWT 토큰에서 사용자 정보 추출 및 검증
        String tokenUid = extractUidFromJWT(authorization);

        // URL의 uid와 JWT 토큰의 uid가 일치하는지 확인
        if (!uid.equals(tokenUid)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        try {
            routineService.addCustomRoutineToUser(uid, request);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @Operation(
            summary = "요청 날짜 기준 사용자의 루틴 정보 반환",
            description = "사용자가 추가했던 루틴들에 대한 정보와, 달성 체크 여부에 대한 데이터를 반환합니다."
    )
    @GetMapping("/personal/{uid}")
    public ResponseEntity<PersonalRoutineResponseDto> getPersonalRoutines(
            @Parameter(description = "사용자 UID", required = true) @PathVariable String uid,
            @RequestHeader("Authorization") String authorization) {

        // JWT 토큰에서 사용자 정보 추출 및 검증
        String tokenUid = extractUidFromJWT(authorization);

        // URL의 uid와 JWT 토큰의 uid가 일치하는지 확인
        if (!uid.equals(tokenUid)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        PersonalRoutineResponseDto responseDto = routineService.getPersonalRoutines(uid);
        return ResponseEntity.ok(responseDto);
    }
    @Operation(
            summary = "사용자 루틴 수정",
            description = "users_routine 테이블에 저장된 정보를 수정합니다."
    )
    @PatchMapping("/personal/{id}")
    public ResponseEntity<Void> updateRoutine(
            @Parameter(description = "루틴 ID", required = true) @PathVariable Integer id,
            @RequestBody RoutineUpdateRequestDto request,
            @RequestHeader("Authorization") String authorization) {

        String tokenUid = extractUidFromJWT(authorization);

        routineService.updateRoutine(id, request, tokenUid);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "사용자 루틴 삭제",
            description = "users_routine 테이블에 저장된 정보를 삭제합니다."
    )
    @DeleteMapping("/personal/{id}")
    public ResponseEntity<Void> deleteRoutine(
            @Parameter(description = "루틴 ID", required = true) @PathVariable Integer id,
            @RequestHeader("Authorization") String authorization) {

        String tokenUid = extractUidFromJWT(authorization);

        routineService.deleteRoutine(id, tokenUid);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Sera의 추천 루틴 정보 반환",
            description = "사용자에게 할당된 Sera의 추천 루틴 정보와 그 달성 체크 여부 정보를 반환합니다."
    )
    @GetMapping("/recommend/{uid}")
    public ResponseEntity<RecommendRoutineResponseDto> getRecommendRoutines(
            @Parameter(description = "사용자 UID", required = true) @PathVariable String uid,
            @RequestHeader("Authorization") String authorization) {

        String tokenUid = extractUidFromJWT(authorization);

        if (!uid.equals(tokenUid)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        RecommendRoutineResponseDto responseDto = routineService.getRecommendRoutines(uid);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Sera의 추천 루틴 정보 반환",
            description = "사용자에게 할당된 Sera의 추천 루틴 정보와 그 달성 체크 여부 정보를 반환합니다."
    )
    @GetMapping("/recommend/{uid}")
    public ResponseEntity<RecommendRoutineResponseDto> getRecommendRoutines(
            @Parameter(description = "사용자 UID", required = true) @PathVariable String uid,
            @RequestHeader("Authorization") String authorization) {

        String tokenUid = extractUidFromJWT(authorization);

        if (!uid.equals(tokenUid)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        RecommendRoutineResponseDto responseDto = routineService.getRecommendRoutines(uid);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Sera의 추천 루틴 달성 체크",
            description = "Sera의 추천 루틴에 대한 달성 체크를 수행하고 추가 lux를 제공합니다."
    )
    @PostMapping("/recommend/attainment/{uid}")
    public ResponseEntity<Void> checkSeraRoutineAttainment(
            @Parameter(description = "사용자 UID", required = true) @PathVariable String uid,
            @RequestBody AttainmentRequestDto request,
            @RequestHeader("Authorization") String authorization) {

        String tokenUid = extractUidFromJWT(authorization);

        if (!uid.equals(tokenUid)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        routineService.checkSeraRoutineAttainment(uid, request.getId());
        return ResponseEntity.noContent().build();
    }

}
