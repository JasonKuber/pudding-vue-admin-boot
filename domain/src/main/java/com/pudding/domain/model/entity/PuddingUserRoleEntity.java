package com.pudding.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PuddingUserRoleEntity {


    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;



}
