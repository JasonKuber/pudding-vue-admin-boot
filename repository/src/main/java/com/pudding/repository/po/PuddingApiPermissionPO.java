package com.pudding.repository.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pudding.repository.base.BaseEntity;
import lombok.Data;

/**
 * 权限表
 * @TableName pudding_permission
 */
@TableName(value ="pudding_api_permission")
@Data
public class PuddingApiPermissionPO extends BaseEntity {
    /**
     * 权限名称
     */
    @TableField(value = "perm_name")
    private String permName;

    /**
     * 权限标识
     */
    @TableField(value = "perm_code")
    private String permCode;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 接口API
     */
    @TableField(value = "perm_api")
    private String permApi;

    /**
     * 请求方式 GET POST PUT DELETE
     */
    @TableField(value = "method")
    private String method;

    /**
     * 是否删除 1=是 2=否
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    /**
     * 创建者id
     */
    @TableField(value = "create_id")
    private Long createId;

    /**
     * 创建者账号
     */
    @TableField(value = "create_account")
    private String createAccount;

    /**
     * 更新者id
     */
    @TableField(value = "update_id")
    private Long updateId;

    /**
     * 更新者账号
     */
    @TableField(value = "update_account")
    private String updateAccount;

}