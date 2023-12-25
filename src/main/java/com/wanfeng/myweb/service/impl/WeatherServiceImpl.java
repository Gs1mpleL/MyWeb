package com.wanfeng.myweb.service.impl;

import com.wanfeng.myweb.Utils.HttpUtils.Requests;
import com.wanfeng.myweb.po.WeatherInfo;
import com.wanfeng.myweb.service.PushService;
import com.wanfeng.myweb.service.WeatherService;
import com.wanfeng.myweb.vo.PushVO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {
    @Autowired
    private Requests requests;
    @Autowired
    private PushService pushService;

    public static List<WeatherInfo> parseWeatherInfo(String html) {
        List<WeatherInfo> weatherInfos = new ArrayList<>();
        try {
            Document document = Jsoup.parse(html);
            Elements days = document.select(".sky");
            for (Element day : days) {
                String date = day.select("h1").text(); // 获取日期，如"19日（今天）"
                String weather = day.select("p.wea").text(); // 获取天气，如"晴"
                String temperature = day.select("p.tem span").text() + "°C"; // 获取温度，如"17℃"
                weatherInfos.add(new WeatherInfo(date, weather, temperature));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weatherInfos;
    }

    @Override
    public void pushWeather() {
        List<WeatherInfo> weatherList = getWeatherList();
        StringBuilder pushMsg = new StringBuilder();
        for (WeatherInfo weatherInfo : weatherList) {
            pushMsg.append(weatherInfo.getDate()).append(":").append(weatherInfo.getWeather()).append(" ").append(weatherInfo.getTemperature()).append("\n");
        }
        String msg = pushMsg.substring(0, pushMsg.length() - 1).toString();
        pushService.pushIphone(new PushVO("近日天气", msg, "近日天气"));
    }

    public List<WeatherInfo> getWeatherList() {
        String url = "http://www.weather.com.cn/weather/101100701.shtml";
        String html = requests.getForHtml(url, null,null);
        return parseWeatherInfo(html);
    }
}
