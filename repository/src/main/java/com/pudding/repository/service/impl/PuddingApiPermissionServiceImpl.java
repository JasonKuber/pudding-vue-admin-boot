package com.pudding.repository.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
        wrapper.eq(PuddingApiPermissionPO::getPermCode, permCode);
        return count(wrapper);
    }

    @Override
    public Long countApiPermissionByPermApi(String permApi) {
        LambdaQueryWrapper<PuddingApiPermissionPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PuddingApiPermissionPO::getPermApi, permApi);
        return count(wrapper);
    }

    @Override
    public Page<PuddingApiPermissionPO> pageApiPermissionList(PuddingApiPermissionPO puddingApiPermissionPO,
                                                              Integer pageNo,
                                                              Integer pageSize) {
        Page<PuddingApiPermissionPO> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<PuddingApiPermissionPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotEmpty(puddingApiPermissionPO.getPermName()),
                        PuddingApiPermissionPO::getPermName, puddingApiPermissionPO.getPermName())
                .like(StrUtil.isNotEmpty(puddingApiPermissionPO.getPermCode()),
                        PuddingApiPermissionPO::getPermCode, puddingApiPermissionPO.getPermCode())
                .like(StrUtil.isNotEmpty(puddingApiPermissionPO.getPermApi()),
                        PuddingApiPermissionPO::getPermApi, puddingApiPermissionPO.getPermApi())
                .eq(StrUtil.isNotEmpty(puddingApiPermissionPO.getMethod()),
                        PuddingApiPermissionPO::getMethod, puddingApiPermissionPO.getMethod())
                .orderByDesc(PuddingApiPermissionPO::getId);
        return page(page, wrapper);
    }

    @Override
    public Boolean updateApiPermissionById(Long id, PuddingApiPermissionPO po) {
        LambdaUpdateWrapper<PuddingApiPermissionPO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(StrUtil.isNotEmpty(po.getPermName()),
                        PuddingApiPermissionPO::getPermName, po.getPermName())
                .set(StrUtil.isNotEmpty(po.getPermCode()),
                        PuddingApiPermissionPO::getPermCode, po.getPermCode())
                .set(StrUtil.isNotEmpty(po.getPermApi()),
                        PuddingApiPermissionPO::getPermApi, po.getPermApi())
                .set(StrUtil.isNotEmpty(po.getDescription()),
                        PuddingApiPermissionPO::getDescription, po.getDescription())
                .set(StrUtil.isNotEmpty(po.getMethod()),
                        PuddingApiPermissionPO::getMethod, po.getMethod())
                .set(PuddingApiPermissionPO::getUpdateId, po.getUpdateId())
                .set(PuddingApiPermissionPO::getUpdateAccount, po.getUpdateAccount())
                .eq(PuddingApiPermissionPO::getId, id);
        return update(new PuddingApiPermissionPO(),updateWrapper);
    }

    @Override
    public Boolean deleteApiPermissionById(Long userId, String account, Long id) {
        LambdaUpdateWrapper<PuddingApiPermissionPO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(PuddingApiPermissionPO::getIsDeleted,1)
                .set(PuddingApiPermissionPO::getUpdateId,userId)
                .set(PuddingApiPermissionPO::getUpdateAccount,account)
                .eq(PuddingApiPermissionPO::getId,id);
        return update(new PuddingApiPermissionPO(),updateWrapper);
    }

    @Override
    public Long countApiPermissionByPermApiAndMethode(String permApi, String method) {
        LambdaQueryWrapper<PuddingApiPermissionPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PuddingApiPermissionPO::getPermApi,permApi)
                .eq(PuddingApiPermissionPO::getMethod,method);
        return count(wrapper);
    }
}




