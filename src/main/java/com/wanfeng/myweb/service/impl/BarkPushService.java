package com.wanfeng.myweb.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.Utils.HttpUtils.Requests;
import com.wanfeng.myweb.config.BizException;
import com.wanfeng.myweb.properties.PushProperties;
import com.wanfeng.myweb.service.PushService;
import com.wanfeng.myweb.vo.PushVO;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Bark消息推送
 */
@Service
public class BarkPushService implements PushService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BarkPushService.class);
    @Autowired
    private Requests requests;
    @Autowired
    private PushProperties pushProperties;

    @Override
    public boolean pushIphone(PushVO pushVO) {
        String url = pushProperties.getIphoneBaseUrl();
        return doPushUsingBark(pushVO, url);
    }

    @Override
    public boolean pushIphone(String quickMsg) {
        String url = pushProperties.getIphoneBaseUrl();
        return doPushUsingBark(new PushVO(quickMsg),url);
    }

    @Override
    public void pushMac(PushVO pushVO) {
        String url = pushProperties.getMacBaseUrl();
        doPushUsingBark(pushVO, url);
    }

    private boolean doPushUsingBark(PushVO pushVO, String url) {
        if (pushVO.getMsg() == null || Objects.equals(pushVO.getMsg(), "")) {
            throw new BizException("没有内容可以发送");
        }
        Map<String, String> map = new HashMap<>();
        map.put("body", pushVO.getMsg());
        if (pushVO.getTitle() != null) {
            map.put("title", pushVO.getTitle());
        }
        if (pushVO.getGroupName() != null) {
            map.put("group", pushVO.getGroupName());
        }
        if (pushVO.getIcon() != null) {
            map.put("icon", pushVO.getIcon());
        } else if (pushProperties.getIcon() != null) {
            map.put("icon", pushProperties.getIcon());
        }
        JSONObject post = requests.post(url, map,null);
        if (post.getString("message").equals("success")){
            LOGGER.info("发送成功[{}]",pushVO);
            return true;
        }else {
            throw  new BizException("发送失败" + post);
        }
    }

}
