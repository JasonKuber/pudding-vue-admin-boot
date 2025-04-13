package com.pudding.repository.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pudding.repository.base.BaseEntity;
import lombok.Data;

/**
 * 角色表
 * @TableName pudding_role
 */
@Data
@TableName(value ="pudding_role")
public class PuddingRolePO extends BaseEntity {
    /**
     * 角色名称
     */
    @TableField(value = "role_name")
    private String roleName;

    /**
     * 创建者用户id
     */
    @TableField(value = "create_by")
    private Long createBy;

}