package com.pudding.api.admin.controller;

import com.pudding.application.admin.service.menu.MenuAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "菜单接口")
@RequestMapping("/menu")
public class MenuController {

    private final MenuAppService menuAppService;


    @GetMapping("/list")
    @ApiOperation("菜单列表")
    public void getMenuList() {

    }




}
