package com.codruwh.routine.controller;

import com.codruwh.routine.application.RecordService;
import com.codruwh.routine.common.ApiException;
import com.codruwh.routine.controller.dto.RecordSleepRequestDto;
import com.codruwh.routine.controller.dto.RecordSleepResponseDto;
import com.codruwh.routine.controller.dto.SleepRecordListResponseDto;
import com.codruwh.routine.domain.UserSleep;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "Record", description = "기록 관련 API")
@RestController
@RequestMapping("/record")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @Operation(
            summary = "수면 시간 저장",
            description = "취침 시각과 기상 시각을 받아 수면 시간을 저장합니다."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/sleep/{uid}")
    public ResponseEntity<RecordSleepResponseDto> recordSleep(
            @Parameter(description = "사용자 고유 식별자") @PathVariable("uid") UUID uid,
            @RequestBody RecordSleepRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID currentUserId = UUID.fromString(userDetails.getUsername());
        if (!currentUserId.equals(uid)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "본인의 기록만 저장할 수 있습니다.");
        }

        UserSleep recordedSleep = recordService.recordSleep(uid, requestDto);
        return new ResponseEntity<>(RecordSleepResponseDto.from(recordedSleep), HttpStatus.CREATED);
    }

    @Operation(
            summary = "오늘의 수면 시간 정보 반환",
            description = "요청 날짜(오늘) 기준 사용자의 수면 시간 정보를 반환합니다."
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/sleep/today/{uid}")
    public ResponseEntity<RecordSleepResponseDto> getSleepRecordForToday(
            @Parameter(description = "사용자 고유 식별자") @PathVariable("uid") UUID uid,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID currentUserId = UUID.fromString(userDetails.getUsername());
        if (!currentUserId.equals(uid)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "본인의 기록만 조회할 수 있습니다.");
        }

        UserSleep sleepRecord = recordService.getSleepRecordForToday(uid);
        return ResponseEntity.ok(RecordSleepResponseDto.from(sleepRecord));
    }

    @Operation(
            summary = "특정 기간의 수면 시간 정보 반환",
            description = "start_day부터 end_day까지의 수면 시간 정보를 리스트로 반환합니다."
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/sleep/{uid}")
    public ResponseEntity<SleepRecordListResponseDto> getSleepRecordsForPeriod(
            @Parameter(description = "사용자 고유 식별자") @PathVariable("uid") UUID uid,
            @Parameter(description = "조회 시작일 (YYYY-MM-DD)") @RequestParam("start_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "조회 종료일 (YYYY-MM-DD)") @RequestParam("end_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID currentUserId = UUID.fromString(userDetails.getUsername());
        if (!currentUserId.equals(uid)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "본인의 기록만 조회할 수 있습니다.");
        }

        List<UserSleep> sleepRecords = recordService.getSleepRecordsForPeriod(uid, startDate, endDate);

        List<RecordSleepResponseDto> dtoList = sleepRecords.stream()
                .map(RecordSleepResponseDto::from)
                .collect(Collectors.toList());

        SleepRecordListResponseDto response = SleepRecordListResponseDto.builder()
                .sleepRecords(dtoList)
                .build();

        return ResponseEntity.ok(response);
    }
}