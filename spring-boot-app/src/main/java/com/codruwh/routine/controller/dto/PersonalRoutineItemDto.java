package java.com.codruwh.routine.controller.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalRoutineItemDto {
    private Integer id;
    private CategoryDto category;
    private String content;
    private String notification; // 현재는 null
    private Boolean complete; // 달성 여부
}
