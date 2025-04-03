package com.pudding.application.admin.service.demo;

import com.pudding.domain.model.vo.GetDemoByIdVO;

public interface DemoAppService {
    GetDemoByIdVO getDemoById(String id);
}
