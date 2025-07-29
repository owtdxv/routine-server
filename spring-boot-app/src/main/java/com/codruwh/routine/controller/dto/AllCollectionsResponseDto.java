package com.codruwh.routine.controller.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class AllCollectionsResponseDto {
    private List<CollectionDetailDto> collections;
}
