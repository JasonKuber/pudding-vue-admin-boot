package com.pudding.domain.model.convert;

import com.pudding.domain.model.entity.DemoEntity;
import com.pudding.domain.model.vo.GetDemoByIdVO;

public class DemoEntityConvert {
    public static GetDemoByIdVO toVo(DemoEntity demoEntity) {
        GetDemoByIdVO vo = new GetDemoByIdVO();
        vo.setName(demoEntity.getName());
        vo.setCreateTime(demoEntity.getCreateTime());
        vo.setUpdateTime(demoEntity.getUpdateTime());
        return vo;
    }
}
