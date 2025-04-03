package com.pudding.manager.login;

import com.pudding.domain.model.entity.PuddingUserEntity;

public interface PasswordLoginManager {

    PuddingUserEntity login(String credentials, String password);


}
