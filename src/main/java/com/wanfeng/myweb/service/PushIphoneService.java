package com.wanfeng.myweb.service;

import com.wanfeng.myweb.Utils.HttpUtil;
import com.wanfeng.myweb.properties.PushToIphoneProperties;
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
    @Autowired
    private PushToIphoneProperties pushToIphoneProperties;
    public String push(PushIphoneVo pushIphoneVo){
        String url = pushToIphoneProperties.getBaseUrl();
        if (pushIphoneVo.getMsg() == null){
            return "请输入内容";
        }
        if (pushIphoneVo.getTitle() !=null){
            url += pushIphoneVo.getTitle();
        }
        url+="/";
        url+=pushIphoneVo.getMsg();
        Map<String,String> map = new HashMap<>();;
        if (pushIphoneVo.getGroupName()!= null){
            map.put("group", pushIphoneVo.getGroupName());
        }
        if (pushIphoneVo.getIcon() != null){
            map.put("icon", pushIphoneVo.getIcon());
        }else if (pushToIphoneProperties.getIcon()!=null){
            map.put("icon", pushToIphoneProperties.getIcon());
        }
        String str = httpUtil.get(url,map);
        if (str.contains("success")){
            return "ok";
        }else {
            return "error";
        }
    }

}
