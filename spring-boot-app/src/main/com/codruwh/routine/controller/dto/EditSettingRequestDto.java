package com.codruwh.routine.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditSettingRequestDto {

  @JsonProperty("title_id")
  private Integer titleId;

  @JsonProperty("background_color")
  private Integer backgroundColor;

  @JsonProperty("lumi_image")
  private Integer lumiImage;
}
