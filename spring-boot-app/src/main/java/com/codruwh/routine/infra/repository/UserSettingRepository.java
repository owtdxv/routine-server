package com.codruwh.routine.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codruwh.routine.domain.UserSetting;

public interface UserSettingRepository extends JpaRepository<UserSetting, String>{

}
