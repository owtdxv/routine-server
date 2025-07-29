package com.codruwh.routine;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class RoutineApplication {

	/**
	 * 애플리케이션의 기본 시간대를 'Asia/Seoul'로 설정합니다.
	 */
	@PostConstruct
	public void setDefaultTimeZone() {
			TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static void main(String[] args) {
		SpringApplication.run(RoutineApplication.class, args);
	}

}
