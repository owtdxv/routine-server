package com.codruwh.routine.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codruwh.routine.application.ChatbotService;
import com.codruwh.routine.common.ApiException;
import com.codruwh.routine.controller.dto.ChatRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
    public ResponseEntity<Map<String, String>> getSerasTip(@AuthenticationPrincipal UserDetails userDetails) {
        String response = chatbotService.getSerasTip();
        return ResponseEntity.ok(Map.of("tip", response));
    }

    @Operation(
        summary = "Sera에게 질의",
        description = "대화 기록을 바탕으로 AI 챗봇에게 답변을 요청합니다"
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{uid}/query")
    public ResponseEntity<Map<String, String>> getChatbotResponseToQuery(@Parameter(description = "사용자 고유 식별자") @PathVariable("uid") UUID uid, @RequestBody ChatRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        // 1. 토큰에서 현재 로그인한 사용자의 UID를 추출합니다.
        UUID currentUserId = UUID.fromString(userDetails.getUsername());

        // 2. 요청 경로의 UID와 토큰의 UID가 일치하는지 검증합니다.
        if (!currentUserId.equals(uid)) {
            // 일치하지 않으면, 다른 사용자의 데이터에 접근하려는 시도이므로 차단합니다.
            throw new ApiException(HttpStatus.FORBIDDEN, "본인 외 사용자의 정보를 요청할 수 없습니다.");
        }

        // 3. 검증이 완료되면, 서비스를 호출합니다.
        String response = chatbotService.getChatbotResponse(requestDto.getConversationHistory(), uid);
        return ResponseEntity.ok(Map.of("response", response));
    }
}
