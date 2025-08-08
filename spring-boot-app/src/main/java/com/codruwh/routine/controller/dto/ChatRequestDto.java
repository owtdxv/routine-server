package com.codruwh.routine.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDto {
    private List<ChatMessageDto> conversationHistory;
}