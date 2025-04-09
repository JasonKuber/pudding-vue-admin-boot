package com.pudding.api.admin.controller;

import com.pudding.application.admin.service.auth.TokenAppService;
import com.pudding.common.annotation.NotAuthentication;
import com.pudding.domain.model.vo.LoginUserVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "授权接口")
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

    private final TokenAppService tokenAppService;

    @ApiOperation("密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "凭证(账号/手机号)",name = "identifier",required = true),
            @ApiImplicitParam(value = "密码",name = "password",required = true),
    })
    @PostMapping("/login/password")
    public LoginUserVO password() {
        // 逻辑交由PasswordAuthenticationLoginFilter 处理
        return new LoginUserVO();
    }


    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public void logOut() {
        // 逻辑交给AdminLogoutHandler处理
    }

    @NotAuthentication
    @PostMapping("/refresh")
    @ApiOperation("使用刷新Token获取访问Token")
    public String refresh(HttpServletRequest request) {
        return tokenAppService.refreshToken(request);
    }




}
