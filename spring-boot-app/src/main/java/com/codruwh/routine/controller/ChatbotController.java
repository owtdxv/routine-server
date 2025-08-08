// src/main/java/com/codruwh/routine/controller/ChatbotController.java

package com.codruwh.routine.controller;

import com.codruwh.routine.application.ChatbotService;
import com.codruwh.routine.common.ApiException; // ApiException import
import com.codruwh.routine.controller.dto.ChatRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID; // UUID import
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; // HttpStatus import
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Annotation import
import org.springframework.security.core.userdetails.UserDetails; // UserDetails import
import org.springframework.web.bind.annotation.PathVariable; // Annotation import
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chatbot", description = "AI 챗봇 관련 API")
@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Operation(
            summary = "AI 챗봇에게 질의",
            description = "대화 기록을 바탕으로 AI 챗봇에게 답변을 요청합니다. AccessToken이 필요합니다."
    )
    @SecurityRequirement(name = "bearerAuth") // Swagger에서 자물쇠 아이콘을 표시
    @PostMapping("/{uid}/query") // 엔드포인트에 사용자 uid를 포함하도록 변경
    public ResponseEntity<String> handleChatQuery(
            @Parameter(description = "사용자 고유 식별자") @PathVariable("uid") UUID uid,
            @RequestBody ChatRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 1. 토큰에서 현재 로그인한 사용자의 UID를 추출합니다.
        UUID currentUserId = UUID.fromString(userDetails.getUsername());

        // 2. 요청 경로의 UID와 토큰의 UID가 일치하는지 검증합니다.
        if (!currentUserId.equals(uid)) {
            // 일치하지 않으면, 다른 사용자의 데이터에 접근하려는 시도이므로 차단합니다.
            throw new ApiException(HttpStatus.FORBIDDEN, "본인 외 사용자의 정보를 요청할 수 없습니다.");
        }

        // 3. 검증이 완료되면, 서비스를 호출합니다. (uid를 함께 전달)
        String botResponse = chatbotService.getChatbotResponse(requestDto.getConversationHistory(), uid);
        return ResponseEntity.ok(botResponse);
    }
}