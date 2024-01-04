package com.wanfeng.myweb.api.dto;


import lombok.Data;

@Data
public class SystemConfigDto {
    private int id;
    private String biliCookie;
    private String yuanShenCookie;
    private String biliRefreshToken;
}
