package com.pudding.domain.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DemoEntity {

    private String name;

    private Date createTime;

    private Date updateTime;

}
