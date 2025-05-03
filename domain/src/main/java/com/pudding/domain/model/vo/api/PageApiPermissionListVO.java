package com.pudding.domain.model.vo.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("API接口权限列表返回前端参数")
public class PageApiPermissionListVO {

    /**
     * API 接口权限Id
     */
    @ApiModelProperty(value = "API接口权限Id")
    private Long apiPermissionId;

    /**
     * 权限名称
     */
    @ApiModelProperty(value = "权限名称")
    private String permName;

    /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识")
    private String permCode;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 接口API
     */
    @ApiModelProperty(value = "接口API")
    private String permApi;

    /**
     * 请求方式 GET POST PUT DELETE
     */
    @ApiModelProperty(value = "请求方式")
    private String method;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者账号")
    private String createAccount;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者账号")
    private String updateAccount;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
