package com.pudding.domain.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PuddingApiPermissionEntity {

    private Long id;

    /**
     * 权限名称
     */
    private String permName;

    /**
     * 权限标识
     */
    private String permCode;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口API
     */
    private String permApi;

    /**
     * 请求方式 GET POST PUT DELETE
     */
    private String method;

    /**
     * 创建者id
     */
    private Long createId;

    /**
     * 创建者账号
     */
    private String createAccount;

    /**
     * 更新者id
     */
    private Long updateId;

    /**
     * 更新者账号
     */
    private String updateAccount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
