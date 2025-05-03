package com.pudding.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("分页数据返回前端")
public class PageResult<T> {

    /**
     * 分页数据
     */
    @ApiModelProperty("分页数据列表")
    private List<T> recordList;

    /**
     * 总条数
     */
    @ApiModelProperty("总条数")
    private Long total;

    /**
     * 当前页
     */
    @ApiModelProperty("当前页")
    private Long current;

    /**
     * 分页大小
     */
    @ApiModelProperty("分页大小")
    private Long size;

    public static <T> PageResult<T> of(List<T> records,
                                       Long total,
                                       Long current,
                                       Long size) {
        return new PageResult<>(records, total, current, size);
    }

}
