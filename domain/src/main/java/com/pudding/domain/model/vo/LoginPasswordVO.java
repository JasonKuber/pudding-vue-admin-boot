package com.pudding.domain.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "登录返回前端参数",description = "密码登录返回前端参数")
public class LoginPasswordVO {

    @ApiModelProperty(value = "账户名")
    private String userName;

    @ApiModelProperty(value = "授权令牌")
    private String token;




}
