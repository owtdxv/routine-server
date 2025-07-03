package com.codruwh.routine.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.codruwh.routine.common.ApiException;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BasicController {

  /**
   * 루트 경로로 들어오는 요청을 처리하여 서버 상태를 반환합니다
   * @return {"status": "UP"} 형태의 JSON 응답, HTTP 200 OK
   */
  @GetMapping("/")
  public ResponseEntity<Map<String, String>> healthCheck() {
    Map<String, String> res = new HashMap<>();
    res.put("status", "UP");

    return ResponseEntity.ok(res);
  }
}
