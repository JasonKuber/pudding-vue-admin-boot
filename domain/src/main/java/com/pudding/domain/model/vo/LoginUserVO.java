package com.pudding.domain.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginUserVO {

    @ApiModelProperty(value = "账户名")
    private String userName;

    @ApiModelProperty(value = "授权令牌")
    private String token;

    @ApiModelProperty(value = "uid")
    private String uid;

    @ApiModelProperty(value = "登录时间")
    private Long loginTime;


}
