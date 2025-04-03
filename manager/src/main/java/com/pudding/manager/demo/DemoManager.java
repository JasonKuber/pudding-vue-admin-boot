package com.pudding.manager.demo;

import com.pudding.domain.model.entity.DemoEntity;


public interface DemoManager {
    DemoEntity getDemoById(String id);
}
