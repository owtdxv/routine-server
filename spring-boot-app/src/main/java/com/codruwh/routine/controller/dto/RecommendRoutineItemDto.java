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
public class RecommendRoutineItemDto {
    private Integer id;
    private com.codruwh.routine.controller.dto.CategoryDto category;
    private String content;
    private Boolean complete;
}
