package com.wanfeng.myweb.Entity;

import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("system_config")
public class SystemConfigEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    @TableField(value = "bili_cookie")
    private String biliCookie;
    @TableField(value = "yuan_shen_cookie")
    private String yuanShenCookie;
}
