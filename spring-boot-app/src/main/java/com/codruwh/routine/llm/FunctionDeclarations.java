// src/main/java/com/codruwh/routine/llm/FunctionDeclarations.java

package com.codruwh.routine.llm;

import com.google.cloud.vertexai.api.FunctionDeclaration;
import com.google.cloud.vertexai.api.Schema;
import com.google.cloud.vertexai.api.Type;

import java.util.Collections;

/**
 * Gemini API의 함수 호출(Function Calling)에 사용될 도구(Tool)들의 명세를 정의하는 클래스입니다.
 * 각 메소드는 특정 API 기능에 대한 명세를 FunctionDeclaration 객체로 반환합니다.
 */
public final class FunctionDeclarations {

    // 이 클래스는 인스턴스화할 필요가 없으므로 생성자를 private으로 선언합니다.
    private FunctionDeclarations() {}

    // =================================================================================
    // 사용자 개인 데이터 조회 API (User-Specific)
    // =================================================================================

    /**
     * 사용자 프로필 조회 기능에 대한 명세입니다.
     * API 문서 2-1. 사용자 프로필 정보 반환
     */
    public static FunctionDeclaration getUserProfile() {
        return FunctionDeclaration.newBuilder()
                .setName("get_user_profile")
                .setDescription("사용자의 프로필 정보(이름, 레벨, lux, 성별, 생년월일 등)를 조회합니다.")
                .build();
    }

    /**
     * 사용자 설정값 조회 기능에 대한 명세입니다.
     * API 문서 2-2. 사용자 설정값 반환
     */
    public static FunctionDeclaration getUserSettings() {
        return FunctionDeclaration.newBuilder()
                .setName("get_user_settings")
                .setDescription("사용자의 설정 정보(칭호, 배경색, 루미 이미지 등)를 조회합니다.")
                .build();
    }

    /**
     * 오늘의 수면 기록 조회 기능에 대한 명세입니다.
     * API 문서 7-2. 수면 시간 정보 반환 (하루)
     */
    public static FunctionDeclaration getTodaySleepRecord() {
        return FunctionDeclaration.newBuilder()
                .setName("get_today_sleep_record")
                .setDescription("오늘 기록된 사용자의 수면 정보를 조회합니다. (취침 시간, 기상 시간, 총 수면 시간 등)")
                .build();
    }

    /**
     * 특정 기간의 수면 기록 조회 기능에 대한 명세입니다.
     * API 문서 7-3. 수면 시간 정보 반환 (특정 기간)
     */
    public static FunctionDeclaration getSleepRecordsForPeriod() {
        return FunctionDeclaration.newBuilder()
                .setName("get_sleep_records_for_period")
                .setDescription("지정한 시작일과 종료일 사이의 모든 수면 기록을 조회합니다.")
                .setParameters(
                    Schema.newBuilder()
                        .setType(Type.OBJECT)
                        .putProperties("start_day", Schema.newBuilder()
                            .setType(Type.STRING)
                            .setDescription("조회를 시작할 날짜 (YYYY-MM-DD 형식)")
                            .build())
                        .putProperties("end_day", Schema.newBuilder()
                            .setType(Type.STRING)
                            .setDescription("조회를 종료할 날짜 (YYYY-MM-DD 형식)")
                            .build())
                        .addRequired("start_day")
                        .addRequired("end_day")
                        .build())
                .build();
    }

    /**
     * 루틴 통계 조회 기능에 대한 명세입니다.
     * API 문서 2-5. 사용자의 루틴 달성 통계 자료 반환
     */
    public static FunctionDeclaration getUserRoutineStatistics() {
        return FunctionDeclaration.newBuilder()
                .setName("get_routine_statistics")
                .setDescription("지정한 시작일과 종료일 사이의 사용자 루틴별 일일 달성 기록을 조회합니다.")
                .setParameters(
                    Schema.newBuilder()
                        .setType(Type.OBJECT)
                        .putProperties("startDay", Schema.newBuilder()
                            .setType(Type.STRING)
                            .setDescription("조회를 시작할 날짜 (YYYY-MM-DD 형식)")
                            .build())
                        .putProperties("endDay", Schema.newBuilder()
                            .setType(Type.STRING)
                            .setDescription("조회를 종료할 날짜 (YYYY-MM-DD 형식)")
                            .build())
                        .addRequired("startDay")
                        .addRequired("endDay")
                        .build())
                .build();
    }

    /**
     * 주간 출석 현황 조회 기능에 대한 명세입니다.
     * API 문서 5-1. 출석부 데이터 반환
     */
    public static FunctionDeclaration getWeeklyAttendance() {
        return FunctionDeclaration.newBuilder()
                .setName("get_weekly_attendance")
                .setDescription("이번 주의 월요일부터 일요일까지의 출석 현황을 조회합니다.")
                .build();
    }

    // =================================================================================
    // 전역 데이터 조회 API (Global) - 누락되었던 부분 추가
    // =================================================================================

    /**
     * 추천 루틴 생성 기능에 대한 명세입니다.
     * API 문서 3-1. 추천 루틴 생성
     */
    public static FunctionDeclaration getRecommendedRoutines() {
        return FunctionDeclaration.newBuilder()
                .setName("get_recommended_routines")
                .setDescription("하나 이상의 카테고리를 지정하면 해당 카테고리에 가중치를 부여한 10개의 추천 루틴을 생성합니다.")
                .setParameters(
                    Schema.newBuilder()
                        .setType(Type.OBJECT)
                        .putProperties("categories", Schema.newBuilder()
                            .setType(Type.ARRAY)
                            .setDescription("추천받고 싶은 카테고리 이름 목록. 허용된 값: [\"수면\", \"운동\", \"영양소\", \"햇빛\", \"사회적유대감\"]")
                            .setItems(Schema.newBuilder().setType(Type.STRING).build())
                            .build())
                        .addRequired("categories")
                        .build())
                .build();
    }

    /**
     * 모든 루틴 정보 조회 기능에 대한 명세입니다.
     * API 문서 3-2. 모든 루틴 정보 반환
     */
    public static FunctionDeclaration getAllRoutines() {
        return FunctionDeclaration.newBuilder()
                .setName("get_all_routines")
                .setDescription("시스템에 정의된 모든 루틴의 목록을 조회합니다.")
                .build();
    }

    /**
     * 루틴 컬렉션(추천 모음) 조회 기능에 대한 명세입니다.
     * API 문서 3-3. Sera의 추천 루틴 모음 반환
     */
    public static FunctionDeclaration getRoutineCollections() {
        return FunctionDeclaration.newBuilder()
                .setName("get_routine_collections")
                .setDescription("Sera가 추천하는 루틴 꾸러미(컬렉션) 목록 전체를 조회합니다.")
                .build();
    }

    /**
     * 오늘의 챌린지 미션 정보 조회 기능에 대한 명세입니다.
     * API 문서 3-4. 챌린지 미션 조회
     */
    public static FunctionDeclaration getChallengeInfo() {
        return FunctionDeclaration.newBuilder()
                .setName("get_challenge_info")
                .setDescription("오늘의 챌린지 미션 내용과 현재까지의 참여자 수를 조회합니다.")
                .build();
    }
}