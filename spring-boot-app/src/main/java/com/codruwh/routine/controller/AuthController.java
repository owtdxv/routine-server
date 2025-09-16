package com.codruwh.routine.controller;

import com.codruwh.routine.application.UserService;
import com.codruwh.routine.common.ApiException;
import com.codruwh.routine.controller.dto.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<Map<String, String>> signUp(@RequestBody(required = false) UserSignUpRequestDto payload) {

      // 입력 값에 대한 유효성 검사
      if(payload == null || payload.getEmail() == null || payload.getEmail().isEmpty() || payload.getPassword() == null || payload.getPassword().isEmpty() || payload.getEmail().isBlank() || payload.getPassword().isBlank()) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "이메일과 비밀번호는 필수 입력 항목입니다.");
      }

      String uid = userService.signUpUser(payload.getEmail(), payload.getPassword());

      // API 명세에 따라 응답 본문을 Map으로 생성합니다.
      Map<String, String> responseBody = new HashMap<>();
      responseBody.put("uid", uid);
      responseBody.put("email", payload.getEmail());

      // 201 Created 상태 코드와 함께 응답을 반환합니다.
      return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
  }

}
