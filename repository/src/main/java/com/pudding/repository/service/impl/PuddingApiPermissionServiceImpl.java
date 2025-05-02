package com.pudding.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pudding.repository.mapper.PuddingApiPermissionMapper;
import com.pudding.repository.po.PuddingApiPermissionPO;
import com.pudding.repository.service.PuddingApiPermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author liuzh
* @description 针对表【pudding_permission(权限表)】的数据库操作Service实现
* @createDate 2025-04-10 16:27:03
*/
@Service
public class PuddingApiPermissionServiceImpl extends ServiceImpl<PuddingApiPermissionMapper, PuddingApiPermissionPO>
    implements PuddingApiPermissionService {

    @Override
    public List<PuddingApiPermissionPO> listPermission() {
        return list();
    }

    @Override
    public Long countApiPermissionByPermCode(String permCode) {
        LambdaQueryWrapper<PuddingApiPermissionPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PuddingApiPermissionPO::getPermCode,permCode);
        return count(wrapper);
    }

    @Override
    public Long countApiPermissionByPermApi(String permApi) {
        LambdaQueryWrapper<PuddingApiPermissionPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PuddingApiPermissionPO::getPermApi,permApi);
        return count(wrapper);
    }
}




