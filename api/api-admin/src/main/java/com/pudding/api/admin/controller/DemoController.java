package com.pudding.api.admin.controller;

import com.pudding.application.admin.service.demo.DemoAppService;
import com.pudding.domain.model.vo.GetDemoByIdVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/demo")
@Api(tags = "Demo 接口控制器")
public class DemoController {

    private final DemoAppService demoAppService;

    @ApiOperation("根据Id查询Demo详情")
    @PostMapping("/getDemo/{id}")
    public GetDemoByIdVO getDemoById(@ApiParam(value = "主键id",required = true) @PathVariable String id) {
        return demoAppService.getDemoById(id);
    }



}
