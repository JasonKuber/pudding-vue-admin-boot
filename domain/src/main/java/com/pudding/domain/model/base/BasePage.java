package com.pudding.domain.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("通用分页类")
public class BasePage {

    @Min(value = 1,message = "{validation.pageNo.min}")
    @NotNull(message = "{validation.pageNo.notNull}")
    @ApiModelProperty("页码")
    private Integer pageNo;

    @Range(min = 10,max = 500,message = "{validation.pageSize.range}")
    @NotNull(message = "{validation.pageSize.notNull}")
    @ApiModelProperty("分页范围")
    private Integer pageSize;


}
