package com.pudding.repository.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pudding.repository.base.BaseEntity;
import lombok.Data;

/**
 * 用户表
 * @TableName pudding_user
 */
@Data
@TableName(value ="pudding_user")
public class PuddingUserPO extends BaseEntity  {
    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 账号
     */
    @TableField(value = "account")
    private String account;

    /**
     * 手机号
     */
    @TableField(value = "phone_number")
    private String phoneNumber;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 状态 1=启用 2=禁用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 是否删除 1=删除 2=未删除
     */
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    /**
     * 创建者Id
     */
    @TableField(value = "create_by")
    private String createBy;


}