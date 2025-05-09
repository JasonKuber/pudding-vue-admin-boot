package com.pudding.api.admin.controller;

import com.pudding.api.admin.controller.base.BaseController;
import com.pudding.application.admin.service.api.ApiPermissionAppService;
import com.pudding.application.admin.service.security.DynamicSecurityMetadataSource;
import com.pudding.common.vo.PageResult;
import com.pudding.domain.model.dto.api.AddApiPermissionDTO;
import com.pudding.domain.model.dto.api.PageApiPermissionListDTO;
import com.pudding.domain.model.dto.api.UpdateApiPermissionDTO;
import com.pudding.domain.model.vo.api.PageApiPermissionListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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
    public PageResult<PageApiPermissionListVO> pageApiPermissionList(@Validated PageApiPermissionListDTO pageApiPermissionListDTO) {
        return apiPermissionAppService.pageApiPermissionList(pageApiPermissionListDTO);
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

    @ApiImplicitParams({
            @ApiImplicitParam(name = "apiPermissionId",value = "API接口权限Id",required = true)
    })
    @PutMapping("/{apiPermissionId}")
    @ApiOperation("修改API接口权限")
    public void updateApiPermission(@PathVariable Long apiPermissionId,
                                    @RequestBody @Valid UpdateApiPermissionDTO updateApiPermissionDTO) {
        apiPermissionAppService.updateApiPermission(getLoginUser(),apiPermissionId,updateApiPermissionDTO);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "apiPermissionId",value = "API接口权限Id",required = true)
    })
    @DeleteMapping("/{apiPermissionId}")
    @ApiOperation("删除API接口权限")
    public void deleteApiPermission(@PathVariable Long apiPermissionId) {
        apiPermissionAppService.deleteApiPermission(getLoginUser(),apiPermissionId);
    }


}
