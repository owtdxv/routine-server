package com.codruwh.routine.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "challenge_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeUser {

    @Id
    @Column(name = "uid")
    private String uid; // PK, FK to users_profile [cite: 39]

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "uid")
    private UserProfile userProfile;

    // 'check'가 SQL 예약어일 수 있으므로 backtick(`)으로 감싸줌
    // 'null' 상태가 가능하므로 Boolean 래퍼 타입 사용 [cite: 45]
    @Column(name = "`check`")
    private Boolean check; // 루틴 달성 여부 [cite: 42]

    @Column(name = "datetime")
    private LocalDateTime datetime; // 날짜 구분용 [cite: 42]
}
