package com.pudding.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pudding.repository.po.DemoPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DemoMapper extends BaseMapper<DemoPO> {
}
