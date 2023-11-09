package com.wanfeng.myweb.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class PushVO {
    private String title;
    private String msg;
    private String groupName;
    private String icon;

    public PushVO(String title, String msg, String groupName) {
        this.title = title;
        this.msg = msg;
        this.groupName = groupName;

    }
}
