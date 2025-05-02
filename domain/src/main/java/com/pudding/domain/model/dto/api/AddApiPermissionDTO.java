package com.pudding.domain.model.dto.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ApiModel("添加API接口权限接收前端参数")
public class AddApiPermissionDTO {

    /**
     * 权限名称
     */
    @NotBlank(message = "{validation.api.permission.permName.notBlank}")
    @Size(min = 1,max = 99,message = "{validation.api.permission.permName.size}")
    @ApiModelProperty(value = "权限名称",required = true)
    private String permName;

    /**
     * 权限唯一标识
     */
    @NotBlank(message = "{validation.api.permission.permCode.notBlank}")
    @Size(min = 1,max = 99,message = "{validation.api.permission.permCode.size}")
    @ApiModelProperty(value = "权限唯一标识",required = true)
    private String permCode;

    /**
     * 描述
     */
    @NotBlank(message = "{validation.api.permission.description.notBlank}")
    @Size(min = 1,max = 200,message = "{validation.api.permission.description.size}")
    @ApiModelProperty(value = "描述",required = true)
    private String description;

    /**
     * 接口API
     */
    @NotBlank(message = "{validation.api.permission.permApi.notBlank}")
    @Size(min = 1,max = 200,message = "{validation.api.permission.permApi.size}")
    @ApiModelProperty(value = "接口API",example = "/api/xxx",required = true)
    private String permApi;

    /**
     * 请求方式 GET POST PUT DELETE
     */
    @NotBlank(message = "{validation.api.permission.method.notBlank}")
    @Size(min = 1,max = 200,message = "{validation.api.permission.method.size}")
    @ApiModelProperty(value = "请求方式",example = "GET POST PUT DELETE",required = true)
    private String method;


}
