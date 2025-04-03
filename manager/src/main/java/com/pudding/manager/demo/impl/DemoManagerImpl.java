package com.pudding.manager.demo.impl;

import com.pudding.common.utils.AssertUtils;
import com.pudding.repository.po.DemoPO;
import com.pudding.repository.service.DemoService;
import com.pudding.domain.model.entity.DemoEntity;
import com.pudding.manager.convert.DemoConvert;
import com.pudding.manager.demo.DemoManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Demo 仓储层实现类
 */
@Service
@RequiredArgsConstructor
public class DemoManagerImpl implements DemoManager {

    private final DemoService demoService;


    @Override
    public DemoEntity getDemoById(String id) {
        DemoPO demoPO = demoService.getById(id);
        AssertUtils.isNull(demoPO,"Id不存在");
        DemoEntity entity = DemoConvert.toEntity(demoPO);
        return entity;
    }
}
