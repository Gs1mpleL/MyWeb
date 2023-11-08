package com.wanfeng.myweb.service;

import com.wanfeng.myweb.Utils.HttpUtil;
import com.wanfeng.myweb.properties.PushToIphoneProperties;
import com.wanfeng.myweb.vo.PushVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class PushIphoneService {
    @Autowired
    private HttpUtil httpUtil;
    @Autowired
    private PushToIphoneProperties pushToIphoneProperties;
    public String pushIphone(PushVO pushVO){
        String url = pushToIphoneProperties.getIphoneBaseUrl();
        return doPush(pushVO,url);
    }
    public String pushMac(PushVO pushVO){
        String url = pushToIphoneProperties.getMacBaseUrl();
        return doPush(pushVO,url);
    }

    private String  doPush(PushVO pushVO, String url) {
        if (pushVO.getMsg() == null | Objects.equals(pushVO.getMsg(), "")){
            return "请输入内容";
        }
        if (pushVO.getTitle() !=null){
            url += pushVO.getTitle();
        }
        url+="/";
        url+= pushVO.getMsg();
        Map<String,String> map = new HashMap<>();
        if (pushVO.getGroupName()!= null){
            map.put("group", pushVO.getGroupName());
        }
        if (pushVO.getIcon() != null){
            map.put("icon", pushVO.getIcon());
        }else if (pushToIphoneProperties.getIcon()!=null){
            map.put("icon", pushToIphoneProperties.getIcon());
        }
        String str = httpUtil.get(url,map);
        if (str.contains("success")){
            return "推送成功";
        }else {
            return "error ";
        }
    }

}
