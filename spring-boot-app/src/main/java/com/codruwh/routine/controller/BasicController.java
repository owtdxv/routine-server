package com.codruwh.routine.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
public class BasicController {

  /**
   * 루트 경로로 들어오는 요청을 처리하여 서버 상태를 반환합니다
   * @return {"status": "UP"} 형태의 JSON 응답, HTTP 200 OK
   */
  @GetMapping("/")
  public ResponseEntity<Map<String, String>> healthCheck() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    Map<String, String> res = new HashMap<>();
    res.put("status", "UP");
    res.put("serverTime", now.format(formatter));
    res.put("timeZone", ZoneId.systemDefault().toString());

    return ResponseEntity.ok(res);
  }
}
