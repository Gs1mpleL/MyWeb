package com.wanfeng.myweb.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushIphoneVo {
    String title;
    String msg;
    String groupName;
}
