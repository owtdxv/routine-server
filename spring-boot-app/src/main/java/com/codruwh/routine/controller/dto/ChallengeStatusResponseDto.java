package java.com.codruwh.routine.controller.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
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

