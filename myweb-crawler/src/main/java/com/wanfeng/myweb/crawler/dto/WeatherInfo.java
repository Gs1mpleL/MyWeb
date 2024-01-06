package com.wanfeng.myweb.crawler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherInfo {
    private String date;
    private String weather;
    private String temperature;
}
