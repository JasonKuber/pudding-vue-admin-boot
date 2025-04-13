package com.pudding.domain.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginUserVO {

    @ApiModelProperty(value = "账户名")
    private String userName;

    @ApiModelProperty(value = "访问令牌")
    private String accessToken;

    @ApiModelProperty(value = "登录时间")
    private Long loginTime;


}
