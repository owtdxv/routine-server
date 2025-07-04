package com.codruwh.routine.controller.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.codruwh.routine.domain.Profile;

import lombok.Getter;
import lombok.Setter;

/**
 * /me에서 사용하는 프로필 정보 반환을 위한 Dto입니다
 */
@Getter
@Setter
public class ProfileResponseDto {
    private UUID id;
    private String displayName;
    private LocalDate birthDate;
    private String gender;
    private OffsetDateTime createdAt;
    private String avatar;
    private String title;
    private Float height;
    private Float weight;
    private String email;

    public ProfileResponseDto(Profile profile) {
        this.id = profile.getId();
        this.displayName = profile.getDisplayName();
        this.birthDate = profile.getBirthDate();
        this.gender = profile.getGender();
        this.createdAt = profile.getCreatedAt();
        this.avatar = profile.getAvatar();
        this.title = profile.getTitle();
        this.height = profile.getHeight();
        this.weight = profile.getWeight();
        this.email = profile.getEmail();
    }
}
