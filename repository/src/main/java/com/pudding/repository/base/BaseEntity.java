package com.pudding.repository.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO,value = "id")
    private Long id;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;


}
