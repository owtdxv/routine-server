package java.com.codruwh.routine.controller.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendRoutineResponseDto {
    private List<RecommendRoutineItemDto> routines;

}
