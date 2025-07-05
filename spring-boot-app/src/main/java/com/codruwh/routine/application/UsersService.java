package com.codruwh.routine.application;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.codruwh.routine.common.ApiException;
import com.codruwh.routine.domain.FirstLoginStatus;
import com.codruwh.routine.domain.Profile;
import com.codruwh.routine.infra.repository.FirstLoginStatusRespository;
import com.codruwh.routine.infra.repository.ProfileRepository;
import com.codruwh.routine.infra.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {

  private final UserRepository userRepository;
  private final ProfileRepository profileRepository;
  private final FirstLoginStatusRespository firstLoginStatusRespository;

  /**
   * 사용자 아이디를 통해 프로필 정보를 조회합니다
   * @param userId 사용자 id
   * @return 사용자 프로필 정보
   */
  public Profile getProfileByUserId(UUID userId) {
    return profileRepository.findById(userId)
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "해당 아이디를 가진 사용자를 찾을 수 없습니다"));
  }

  /**
   * 사용자 아이디를 통해 최초 로그인 여부를 조회합니다
   * @param userId 사용자 id
   * @return 사용자 Id와 최초 로그인 여부
   */
  public FirstLoginStatus getIsFirstLoginStatus(UUID userId) {
    return firstLoginStatusRespository.findById(userId)
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "해당 아이디를 가진 사용자를 찾을 수 없습니다"));
  }
}
