package com.pudding.manager.auth.login.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import com.pudding.domain.model.entity.PuddingUserEntity;
import com.pudding.manager.auth.login.PasswordLoginManager;
import com.pudding.manager.convert.PuddingUserConvert;
import com.pudding.repository.cache.user.PuddingUserCache;
import com.pudding.repository.po.PuddingUserPO;
import com.pudding.repository.service.PuddingUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PasswordLoginManagerImpl implements PasswordLoginManager {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private final PuddingUserService puddingUserService;

    private final PasswordEncoder passwordEncoder;

    private final PuddingUserCache puddingUserCache;

    @Override
    public PuddingUserEntity login(String credentials, String password) {
        PuddingUserPO puddingUserPO = null;
        if (PhoneUtil.isPhone(credentials)) {
            // 使用手机号登录
            puddingUserPO = puddingUserService.getByPhone(credentials);
        } else if (EMAIL_PATTERN.matcher(credentials).matches()) {
            // 使用邮箱登录
            puddingUserPO = puddingUserService.getByEmail(credentials);
        } else {
            // 使用账号登录
            puddingUserPO = puddingUserService.getByAccount(credentials);
        }

        if (ObjectUtil.isNull(puddingUserPO)) {
            throw new BadCredentialsException("账号或密码错误");
        }

        if (passwordEncoder.matches(passwordEncoder.encode(password), puddingUserPO.getPassword())) {
            throw new BadCredentialsException("账号或密码错误");
        }

        puddingUserCache.cacheById(puddingUserPO.getId().toString(),puddingUserPO);

        PuddingUserEntity entity = PuddingUserConvert.toEntity(puddingUserPO);
        if (!entity.getStatus()) {
            throw new DisabledException("账号已被禁用");
        }


        return entity;
    }
}
