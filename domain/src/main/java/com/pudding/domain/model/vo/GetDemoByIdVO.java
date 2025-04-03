package com.pudding.domain.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "Demo返回前端参数",description = "根据Id查询并返回Demo信息")
public class GetDemoByIdVO {

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
