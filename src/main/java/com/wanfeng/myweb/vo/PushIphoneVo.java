package com.wanfeng.myweb.vo;

import com.wanfeng.myweb.properties.BiliProperties;
import com.wanfeng.myweb.properties.PushToIphoneProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class PushIphoneVo {
    private String title;
    private String msg;
    private String groupName;
    private String icon;

    public PushIphoneVo(String title, String msg, String groupName) {
        this.title = title;
        this.msg = msg;
        this.groupName = groupName;
    }
}
