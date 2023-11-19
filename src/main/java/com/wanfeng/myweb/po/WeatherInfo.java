package com.wanfeng.myweb.po;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherInfo {
    private String date;
    private String weather;
    private String temperature;
}
