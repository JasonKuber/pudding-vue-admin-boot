package com.pudding.api.admin.controller;

import com.pudding.domain.model.vo.LoginUserVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "授权接口")
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

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




}
