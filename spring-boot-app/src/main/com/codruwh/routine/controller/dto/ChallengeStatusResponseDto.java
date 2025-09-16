package com.codruwh.routine.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeStatusResponseDto {
    private ChallengeInfoDto challenge;
    private Integer participants;
    private Boolean isTarget;
    private Boolean check;
}

