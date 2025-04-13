package com.pudding.repository.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色权限关联表
 * @TableName pudding_permission_role
 */
@TableName(value ="pudding_permission_role")
@Data
public class PuddingPermissionRolePO {
    /**
     * 权限id
     */
    @TableField(value = "perm_id")
    private Long permId;

    /**
     * 角色id
     */
    @TableField(value = "role_id")
    private Long roleId;

}