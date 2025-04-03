package com.pudding.application.admin.service.demo.impl;

import com.pudding.application.admin.service.demo.DemoAppService;
import com.pudding.domain.model.convert.DemoEntityConvert;
import com.pudding.domain.model.entity.DemoEntity;
import com.pudding.domain.model.vo.GetDemoByIdVO;
import com.pudding.manager.demo.DemoManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemoAppServiceImpl implements DemoAppService {

    private final DemoManager demoService;

    @Override
    public GetDemoByIdVO getDemoById(String id) {
        DemoEntity demoEntity = demoService.getDemoById(id);
        return DemoEntityConvert.toVo(demoEntity);
    }
}
