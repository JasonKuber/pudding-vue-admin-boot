package com.pudding.repository.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pudding.repository.base.BaseEntity;
import lombok.Data;

@Data
@TableName("pudding_demo")
public class DemoPO extends BaseEntity {

    @TableField("name")
    private String name;


}
