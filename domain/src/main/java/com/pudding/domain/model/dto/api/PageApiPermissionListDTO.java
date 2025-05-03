package com.pudding.domain.model.dto.api;

import com.pudding.domain.model.base.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ApiModel("API接口权限列表接收前端参数")
public class PageApiPermissionListDTO extends BasePage {

    /**
     * 权限名称
     */
    @Size(min = 1, max = 99,message = "{validation.api.permission.permName.size}")
    @ApiModelProperty(value = "权限名称")
    private String permName;

    /**
     * 权限标识
     */
    @Size(min = 1, max = 99,message = "{validation.api.permission.permName.size}")
    @ApiModelProperty(value = "权限标识")
    private String permCode;

    /**
     * 接口API
     */
    @Size(min = 1, max = 200,message = "{validation.api.permission.permName.size}")
    @ApiModelProperty(value = "接口API")
    private String permApi;

    /**
     * 请求方式 GET POST PUT DELETE
     */
    @Pattern(regexp = "^(GET|POST|PUT|DELETE)$",message = "{validation.api.permission.method.pattern}")
    @ApiModelProperty(value = "请求方式")
    private String method;


}
