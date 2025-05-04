package com.pudding.domain.model.dto.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ApiModel(value = "修改API接口权限接收前端参数")
public class UpdateApiPermissionDTO {

    /**
     * 权限名称
     */
    @Size(max = 99,message = "{validation.api.permission.permName.size}")
    @ApiModelProperty(value = "权限名称")
    private String permName;

    /**
     * 权限唯一标识
     */
    @Size(max = 99,message = "{validation.api.permission.permCode.size}")
    @ApiModelProperty(value = "权限唯一标识")
    private String permCode;

    /**
     * 描述
     */
    @Size(max = 200,message = "{validation.api.permission.description.size}")
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 接口API
     */
    @Size(max = 200,message = "{validation.api.permission.permApi.size}")
    @ApiModelProperty(value = "接口API",example = "/api/xxx")
    private String permApi;

    /**
     * 请求方式 GET POST PUT DELETE
     */
    @Size(max = 200,message = "{validation.api.permission.method.size}")
    @Pattern(regexp = "^(GET|POST|PUT|DELETE)$",message = "{validation.api.permission.method.pattern}")
    @ApiModelProperty(value = "请求方式",example = "GET")
    private String method;



}
