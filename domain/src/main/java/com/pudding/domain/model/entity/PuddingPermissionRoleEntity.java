package com.pudding.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PuddingPermissionRoleEntity {


    /**
     * 权限id
     */
    private Long permId;

    /**
     * 角色id
     */
    private Long roleId;



}
