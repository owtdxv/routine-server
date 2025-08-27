package com.codruwh.routine.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codruwh.routine.application.ChatbotService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name="Sera", description = "AI 챗봇 관련 API")
@RestController
@RequestMapping("/sera")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;
    
    @Operation(
        summary = "Sera의 Tip!",
        description = "메인화면에 표시되는 Sera의 건강 팁을 반환합니다"
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/tip")
    public ResponseEntity<String> getSerasTip(@AuthenticationPrincipal UserDetails userDetails) {
        String response = chatbotService.getSerasTip();
        return ResponseEntity.ok(response);
    }
}
