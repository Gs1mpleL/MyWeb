package com.wanfeng.myweb.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.api.dto.PushVO;
import com.wanfeng.myweb.common.Utils.HttpUtils.Requests;
import com.wanfeng.myweb.common.exception.BizException;
import com.wanfeng.myweb.user.properties.PushProperties;
import com.wanfeng.myweb.user.service.PushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Bark消息推送
 */
@Service
public class BarkPushService implements PushService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BarkPushService.class);
    @Resource
    private Requests requests;
    @Resource
    private PushProperties pushProperties;

    @Override
    public boolean pushIphone(PushVO pushVO) {
        String url = pushProperties.getIphoneBaseUrl();
        return doPushUsingBark(pushVO, url);
    }

    @Override
    public boolean pushIphone(String quickMsg) {
        String url = pushProperties.getIphoneBaseUrl();
        return doPushUsingBark(new PushVO(quickMsg), url);
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
        JSONObject post = requests.post(url, map, null);
        if (post.getString("message").equals("success")) {
            return true;
        } else {
            throw new BizException("发送失败" + post);
        }
    }

}
