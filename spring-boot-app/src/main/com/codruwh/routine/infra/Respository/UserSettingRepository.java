package com.codruwh.routine.infra.Respository;

import com.codruwh.routine.domain.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingRepository extends JpaRepository<UserSetting, String>{

}
