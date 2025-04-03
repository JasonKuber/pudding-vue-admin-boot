package com.pudding.repository.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pudding.repository.mapper.DemoMapper;
import com.pudding.repository.po.DemoPO;
import com.pudding.repository.service.DemoService;
import org.springframework.stereotype.Service;

/**
 * Demo 仓储层实现类
 */
@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, DemoPO>
        implements DemoService {


}
