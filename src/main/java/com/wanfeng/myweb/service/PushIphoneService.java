package com.wanfeng.myweb.service;

import com.wanfeng.myweb.Utils.HttpUtil;
import com.wanfeng.myweb.vo.PushIphoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PushIphoneService {
    @Autowired
    private HttpUtil httpUtil;
    @Value("${push.iphone.baseUrl}")
    private String baseUrl;

    /**
     * 给Iphone发送消息
     */
    public String push(PushIphoneVo pushIphoneVo){
        String url = baseUrl;
        if (pushIphoneVo.getMsg() == null){
            return "请输入内容";
        }
        if (pushIphoneVo.getTitle() !=null){
            url += pushIphoneVo.getTitle();
        }
        url+="/";
        url+=pushIphoneVo.getMsg();
        Map<String,String> map = null;
        if (pushIphoneVo.getGroupName()!= null){
            map = new HashMap<>();
            map.put("group", pushIphoneVo.getGroupName());
        }
        String str = httpUtil.get(url,map);
        if (str.contains("success")){
            return "Ok";
        }else {
            return "error";
        }
    }

}
