package com.pudding.domain.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "登录接收前端参数",description = "使用密码进行登录")
public class LoginPasswordDTO {

    @ApiModelProperty(value = "登录凭证 (账号/手机号/邮箱)",required = true)
    private String credentials;

    @ApiModelProperty(value = "登录密码")
    private String password;


}
