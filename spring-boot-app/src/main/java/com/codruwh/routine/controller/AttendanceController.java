package com.codruwh.routine.controller;

import com.codruwh.routine.application.AttendanceService;
import com.codruwh.routine.application.UserService;
import com.codruwh.routine.common.ApiException;
import com.codruwh.routine.controller.dto.AttendanceCheckResponseDto;
import com.codruwh.routine.controller.dto.AttendanceResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Attendance", description = "출석 체크 관련 API")
@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final UserService userService;
    private final AttendanceService attendanceService;

    @Operation(
            summary = "출석부 데이터 반환",
            description = "사용자의 월요일부터 일요일까지의 주간 출석부 데이터를 반환합니다."
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{uid}")
    public ResponseEntity<AttendanceResponseDto> getAttendanceStatus(
            @Parameter(description = "사용자 고유 식별자") @PathVariable("uid") UUID uid,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 1. 본인의 데이터만 조회할 수 있도록 보안 검증을 수행합니다.
        UUID currentUserId = UUID.fromString(userDetails.getUsername());
        if (!currentUserId.equals(uid)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "본인의 출석부만 조회할 수 있습니다.");
        }

        // 2. 서비스 로직을 호출하여 출석부 데이터를 가져옵니다.
        AttendanceResponseDto responseDto = attendanceService.getAttendanceStatus(uid);

        return ResponseEntity.ok(responseDto);
    }


    @Operation(
            summary = "출석 체크",
            description = "요청을 보낸 요일에 대한 출석 체크를 수행하고, 일요일에 주간 개근 시 보너스 lux를 지급합니다."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{uid}")
    public ResponseEntity<AttendanceCheckResponseDto> checkAttendance(
            @Parameter(description = "사용자 고유 식별자") @PathVariable("uid") UUID uid,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 1. 본인의 데이터만 수정할 수 있도록 보안 검증을 수행합니다.
        UUID currentUserId = UUID.fromString(userDetails.getUsername());
        if (!currentUserId.equals(uid)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "본인의 출석만 체크할 수 있습니다.");
        }

        // 2. 서비스 로직을 호출하여 출석 체크를 수행합니다.
        AttendanceCheckResponseDto responseDto = attendanceService.checkAttendance(uid);

        return ResponseEntity.ok(responseDto);
    }
}
