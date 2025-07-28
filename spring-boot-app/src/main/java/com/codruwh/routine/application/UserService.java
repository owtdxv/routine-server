package com.codruwh.routine.application;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.codruwh.routine.common.ApiException;
import com.codruwh.routine.controller.dto.UserProfileResponseDto;
import com.codruwh.routine.controller.dto.UserSettingResponseDto;
import com.codruwh.routine.domain.Title;
import com.codruwh.routine.domain.UserProfile;
import com.codruwh.routine.domain.UserSetting;
import com.codruwh.routine.infra.repository.TitleRepository;
import com.codruwh.routine.infra.repository.UserProfileRepository;
import com.codruwh.routine.infra.repository.UserSettingRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SupabaseAuthService supabaseAuthService;
    private final UserProfileRepository userProfileRepository;
    private final UserSettingRepository userSettingRepository;
    private final TitleRepository titleRepository;

    /**
     * Supabase Authentication에 새 사용자를 생성하고,
     * users_profile 및 users_setting 테이블에 초기 데이터를 생성합니다.
     *
     * @param email 사용자 이메일
     * @param password 사용자의 비밀번호
     * @return 생성된 사용자의 UID: Mono<String>
     */
    @Transactional
    public String signUpUser(String email, String password) {
        // 1. Supabase Auth를 통해 사용자 생성 및 UID 반환
        String uid = supabaseAuthService.createUser(email, password);

        // 2. UserProfile 엔티티 생성 및 저장
        // uid와 email을 설정하고, 나머지는 엔티티에 정의된 기본값 또는 @PrePersist로 자동 설정됩니다.
        UserProfile userProfile = UserProfile.builder()
                .uid(uid)
                .email(email)
                .build();
        // saveAndFlush를 사용하여 즉시 DB에 INSERT 쿼리를 보내고, 영속성 컨텍스트에 반영합니다.
        // 이를 통해 UserSetting 저장 전에 UserProfile이 확실히 존재함을 보장합니다.
        UserProfile savedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // 3. 기본 칭호(Title) 조회
        // ID가 0인 칭호를 기본값으로 가정합니다. 해당 칭호가 DB에 반드시 존재해야 합니다.
        Title defaultTitle = titleRepository.findById(0)
                .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "기본 칭호(ID: 0)를 찾을 수 없습니다."));

        // 4. UserSetting 엔티티 생성 및 저장
        // @MapsId를 사용하므로 userProfile 객체를 연결해주면 uid가 자동으로 매핑됩니다.
        UserSetting userSetting = UserSetting.builder()
                .userProfile(savedUserProfile)
                .title(defaultTitle) // 조회한 기본 칭호 설정
                .backgroundColor(0)
                .lumiImage(0)
                .build();
        userSettingRepository.save(userSetting);

        // 5. 생성된 UID 반환
        return uid;
    }

    /**
     * 사용자 고유 식별자를 통해 사용자 프로필 정보를 반환합니다.
     * @param uid 사용자 고유 식별자
     * @return profile 테이블의 UID를 제외한 나머지 정보
     */
    public UserProfileResponseDto getUserProfileById(UUID uid) {
        UserProfile profile = userProfileRepository.findById(uid.toString())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "사용자 프로필 정보를 찾을 수 없습니다."));

        return UserProfileResponseDto.from(profile);
    }

    /**
     * 사용자 고유 식별자를 통해 사용자 설정값 정보를 반환합니다.
     * @param uid 사용자 고유 식별자
     * @return settings 테이블의 UID를 제외한 나머지 정보
     */
    public UserSettingResponseDto getUserSettingById(UUID uid) {
        UserSetting userSetting = userSettingRepository.findById(uid.toString())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "사용자 프로필 정보를 찾을 수 없습니다."));

        return UserSettingResponseDto.from(userSetting);
    }
}
