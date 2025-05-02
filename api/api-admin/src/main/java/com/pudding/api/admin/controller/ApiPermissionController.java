package com.pudding.api.admin.controller;

import com.pudding.api.admin.controller.base.BaseController;
import com.pudding.application.admin.service.api.ApiPermissionAppService;
import com.pudding.application.admin.service.security.DynamicSecurityMetadataSource;
import com.pudding.domain.model.dto.api.AddApiPermissionDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Api(tags = "API权限接口")
@RequestMapping("/api-permission")
public class ApiPermissionController extends BaseController {

    private final ApiPermissionAppService apiPermissionAppService;

    private final DynamicSecurityMetadataSource dynamicSecurityMetadataSource;

    @GetMapping
    @ApiOperation("API接口权限列表")
    public void getApiPermissionList() {

    }

    @PostMapping
    @ApiOperation("添加API接口权限")
    public void addApiPermission(@RequestBody @Valid AddApiPermissionDTO addApiPermissionDTO) {
        apiPermissionAppService.addApiPermission(getLoginUser(),addApiPermissionDTO);
    }

    @PutMapping("/refresh")
    @ApiOperation("刷新API接口权限")
    public void refreshPermissions() {
        dynamicSecurityMetadataSource.refresh();
    }

    @PutMapping
    @ApiOperation("修改API接口权限")
    public void updateApiPermission() {

    }

    @DeleteMapping
    @ApiOperation("删除API接口权限")
    public void deleteApiPermission() {

    }


}
