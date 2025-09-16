package com.codruwh.routine.application;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codruwh.routine.common.ApiException;
import com.codruwh.routine.controller.dto.CategoryDto;
import com.codruwh.routine.controller.dto.ChallengeResponseDto;
import com.codruwh.routine.domain.RoutineChallenge;
import com.codruwh.routine.infra.repository.ChallengeUserRepository;
import com.codruwh.routine.infra.repository.RoutineChallengeRepository;

import lombok.RequiredArgsConstructor;

import com.codruwh.routine.controller.dto.ChallengeStatusResponseDto;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final RoutineChallengeRepository routineChallengeRepository;
    private final ChallengeUserRepository challengeUserRepository;

    /**
     * 현재 챌린지 미션의 정보와 오늘 완료한 참여자 수를 조회합니다.
     *
     * @return 챌린지 정보가 담긴 DTO
     */
    @Transactional(readOnly = true)
    public ChallengeResponseDto getChallengeInfo() {
        // 1. 현재 진행 중인 챌린지 미션을 조회합니다.
        RoutineChallenge currentChallenge = routineChallengeRepository.findFirstBy()
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "챌린지 미션 정보가 존재하지 않습니다"));

        // 2. 오늘 챌린지를 완료한(check=true) 참여자 수를 조회합니다.
        // challenge_users 테이블은 매일 초기화되므로 날짜 조건 없이 count합니다.
        long participantCount = challengeUserRepository.countByCheckIsTrue();

        // 3. 응답 DTO를 조립합니다.
        CategoryDto categoryDto = CategoryDto.builder()
                .categoryId(currentChallenge.getCategory().getCategoryId())
                .value(currentChallenge.getCategory().getValue())
                .build();

        return ChallengeResponseDto.builder()
                .category(categoryDto)
                .content(currentChallenge.getContent())
                .participant(participantCount)
                .build();
    }

    public ChallengeStatusResponseDto getChallengeStatus(String uid) {
        // 이번 달의 챌린지 정보 가져오기
        RoutineChallenge currentChallenge = routineChallengeRepository.findCurrentChallenge()
                .orElse(null);

        if (currentChallenge == null) {
            throw new IllegalStateException("진행 중인 챌린지가 없습니다.");
        }

        // 카테고리 정보
        Category category = categoryRepository.findById(currentChallenge.getCategoryId())
                .orElse(null);

        CategoryDto categoryDto = null;
        if (category != null) {
            categoryDto = CategoryDto.builder()
                    .categoryId(category.getId())
                    .value(category.getValue())
                    .build();
        }

        ChallengeInfoDto challengeDto = ChallengeInfoDto.builder()
                .category(categoryDto)
                .content(currentChallenge.getContent())
                .build();

        // 총 참여자 수 (오늘 기준)
        LocalDate today = LocalDate.now();
        int participants = challengeUserRepository.countByDateBetween(
                today.atStartOfDay(), today.plusDays(1).atStartOfDay());

        // 이 사용자가 대상자인지 확인
        ChallengeUser challengeUser = challengeUserRepository.findByUidAndDateBetween(
                        uid, today.atStartOfDay(), today.plusDays(1).atStartOfDay())
                .orElse(null);

        boolean isTarget = challengeUser != null;
        boolean check = false;

        if (isTarget) {
            check = challengeUser.getCheck() != null; // check 값이 null이 아니면 선택을 한 것
        }

        return ChallengeStatusResponseDto.builder()
                .challenge(challengeDto)
                .participants(participants)
                .isTarget(isTarget)
                .check(check)
                .build();
    }

    @Transactional
    public void acceptChallenge(String uid) {
        LocalDate today = LocalDate.now();
        ChallengeUser challengeUser = challengeUserRepository.findByUidAndDateBetween(
                        uid, today.atStartOfDay(), today.plusDays(1).atStartOfDay())
                .orElseThrow(() -> new IllegalArgumentException("챌린지 대상자가 아닙니다."));

        challengeUser.setCheck(false); // 도전 수락, 아직 달성하지 않음
        challengeUserRepository.save(challengeUser);
    }

    @Transactional
    public void holdChallenge(String uid) {
        LocalDate today = LocalDate.now();
        challengeUserRepository.deleteByUidAndDateBetween(
                uid, today.atStartOfDay(), today.plusDays(1).atStartOfDay());
    }
    @Transactional
    public void checkChallengeAttainment(String uid) {
        LocalDate today = LocalDate.now();
        ChallengeUser challengeUser = challengeUserRepository.findByUidAndDateBetween(
                        uid, today.atStartOfDay(), today.plusDays(1).atStartOfDay())
                .orElseThrow(() -> new IllegalArgumentException("챌린지에 참여하지 않았습니다."));

        if (challengeUser.getCheck() == null) {
            throw new IllegalArgumentException("챌린지를 수락하지 않았습니다.");
        }

        if (challengeUser.getCheck()) {
            throw new IllegalArgumentException("이미 달성한 챌린지입니다.");
        }

        challengeUser.setCheck(true); // 달성 완료
        challengeUserRepository.save(challengeUser);
    }
}
