package com.pudding.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResult<T> {

    /**
     * 分页数据
     */
    private List<T> recordList;

    /**
     * 总条数
     */
    private Long total;

    /**
     * 当前页
     */
    private Long current;

    /**
     * 分页大小
     */
    private Long size;

    public static <T> PageResult<T> of(List<T> records,
                                       Long total,
                                       Long current,
                                       Long size) {
        return new PageResult<>(records, total, current, size);
    }

}
