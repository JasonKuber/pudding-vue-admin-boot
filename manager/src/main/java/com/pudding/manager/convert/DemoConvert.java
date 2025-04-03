package com.pudding.manager.convert;

import com.pudding.repository.po.DemoPO;
import com.pudding.domain.model.entity.DemoEntity;

public class DemoConvert {


    public static DemoEntity toEntity(DemoPO demoPO) {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setName(demoPO.getName());
        demoEntity.setCreateTime(demoPO.getCreateTime());
        demoEntity.setUpdateTime(demoPO.getUpdateTime());
        return demoEntity;
    }
}
