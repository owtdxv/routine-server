package com.codruwh.routine.infra.Respository;

import com.codruwh.routine.domain.ChallengeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ChallengeUserRepository extends JpaRepository<ChallengeUser, String> {
    /**
     * 챌린지를 완료(check=true)한 사용자 수를 계산합니다.
     * challenge_users 테이블의 데이터는 매일 초기화되므로, 날짜 조건 없이 조회합니다.
     * @return 완료한 사용자 수
     */
    long countByCheckIsTrue();
    Optional<ChallengeUser> findByUidAndDateBetween(String uid, LocalDateTime start, LocalDateTime end);
    int countByDateBetween(LocalDateTime start, LocalDateTime end);

    void deleteByUidAndDateBetween(String uid, LocalDateTime start, LocalDateTime end);
}
