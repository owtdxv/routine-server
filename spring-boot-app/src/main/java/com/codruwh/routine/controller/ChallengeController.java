package com.codruwh.routine.controller;

import com.codruwh.routine.application.ChallengeService;
import com.codruwh.routine.common.ApiException;
import com.codruwh.routine.controller.dto.ChallengeStatusResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Challenge",
        description = "챌린지 미션 관련 API"
)
@RestController
@RequestMapping("/routine/challenge")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    /**
     * 4-11. 챌린지 미션 발생 여부 조회
     * 이 사용자가 오늘 챌린지 미션이 발생했는지 아닌지를 확인합니다.
     */
    @Operation(
            summary = "챌린지 미션 발생 여부 조회",
            description = "이 사용자가 오늘 챌린지 미션이 발생했는지 아닌지를 확인합니다."
    )
    @GetMapping("/istarget/{uid}")
    public ResponseEntity<ChallengeStatusResponseDto> getChallengeStatus(
            @Parameter(description = "사용자 UID", required = true) @PathVariable String uid,
            @RequestHeader("Authorization") String authorization) {

        // JWT 토큰에서 사용자 정보 추출 및 검증
        String tokenUid = extractUidFromJWT(authorization);

        // URL의 uid와 JWT 토큰의 uid가 일치하는지 확인
        if (!uid.equals(tokenUid)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        ChallengeStatusResponseDto responseDto = challengeService.getChallengeStatus(uid);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 4-12. 챌린지 미션 도전 수락
     * 챌린지 미션을 진행하기로 결정했다는 사실을 DB에 기록합니다.
     */
    @Operation(
            summary = "챌린지 미션 도전 수락",
            description = "챌린지 미션을 진행하기로 결정했다는 사실을 DB에 기록합니다."
    )
    @PatchMapping("/acceptance/{uid}")
    public ResponseEntity<Void> acceptChallenge(
            @Parameter(description = "사용자 UID", required = true) @PathVariable String uid,
            @RequestHeader("Authorization") String authorization) {

        // JWT 토큰에서 사용자 정보 추출 및 검증
        String tokenUid = extractUidFromJWT(authorization);

        // URL의 uid와 JWT 토큰의 uid가 일치하는지 확인
        if (!uid.equals(tokenUid)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        challengeService.acceptChallenge(uid);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * 4-13. 챌린지 미션 다음에
     * 챌린지 선택창에서 "다음에"를 눌렀을 때 호출할 API입니다.
     */
    @Operation(
            summary = "챌린지 미션 다음에",
            description = "챌린지 선택창의 선택지 중 '다음에'를 눌렀을 때 호출할 API입니다."
    )
    @DeleteMapping("/hold/{uid}")
    public ResponseEntity<Void> holdChallenge(
            @Parameter(description = "사용자 UID", required = true) @PathVariable String uid,
            @RequestHeader("Authorization") String authorization) {

        // JWT 토큰에서 사용자 정보 추출 및 검증
        String tokenUid = extractUidFromJWT(authorization);

        // URL의 uid와 JWT 토큰의 uid가 일치하는지 확인
        if (!uid.equals(tokenUid)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        challengeService.holdChallenge(uid);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * 4-14. 챌린지 미션 달성 체크
     * 챌린지 미션에 대한 달성 체크를 합니다.
     */
    @Operation(
            summary = "챌린지 미션 달성 체크",
            description = "챌린지 미션에 대한 달성 체크를 합니다. check값을 true로 변경합니다."
    )
    @PatchMapping("/attainment/{uid}")
    public ResponseEntity<Void> checkChallengeAttainment(
            @Parameter(description = "사용자 UID", required = true) @PathVariable String uid,
            @RequestHeader("Authorization") String authorization) {

        // JWT 토큰에서 사용자 정보 추출 및 검증
        String tokenUid = extractUidFromJWT(authorization);

        // URL의 uid와 JWT 토큰의 uid가 일치하는지 확인
        if (!uid.equals(tokenUid)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        challengeService.checkChallengeAttainment(uid);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * JWT에서 UID 추출하는 메서드 (기존 구현이 있다면 사용)
     */
    private String extractUidFromJWT(String authorization) {
        // "Bearer " 제거
        String token = authorization.substring(7);
        // JWT 파싱 로직 (기존 구현 사용)
        // 예시: return jwtUtil.getUidFromToken(token);
        return ""; // 실제 구현 필요
    }
}