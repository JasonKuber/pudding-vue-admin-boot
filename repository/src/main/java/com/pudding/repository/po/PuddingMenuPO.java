package com.pudding.repository.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pudding.repository.base.BaseEntity;
import lombok.Data;

/**
 * 菜单表
 * @TableName pudding_menu
 */
@Data
@TableName(value ="pudding_menu")
public class PuddingMenuPO extends BaseEntity {


    /**
     * 上级菜单id，一级使用0
     */
    @TableField(value = "parent_menu_id")
    private Long parentMenuId;

    /**
     * 菜单名称
     */
    @TableField(value = "menu_name")
    private String menuName;

    /**
     * 菜单路由
     */
    @TableField(value = "menu_router")
    private String menuRouter;

    /**
     * 菜单图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 显示顺序
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 是否启用
     */
    @TableField(value = "status")
    private Integer status;

}