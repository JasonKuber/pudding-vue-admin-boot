package com.pudding.domain.model.entity;

import lombok.Data;

@Data
public class PuddingUserEntity {

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 账号
     */
    private String account;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态
     */
    private Boolean status;

    /**
     * 是否为admin
     */
    private Boolean isAdmin;


    /**
     * 判断是否开启
     * @param status 1=开启 2=关闭
     * @return
     */
    public Boolean enable(Integer status) {

        return status.equals(1);
    }


    /**
     * 判断是否为Admin角色
     * @param isAdmin 1=是 2=否
     * @return
     */
    public Boolean isAdmin(Integer isAdmin) {
        return isAdmin.equals(1);
    }





}
