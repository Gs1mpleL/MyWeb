package com.wanfeng.myweb.common.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCodeEnum {
    /**
     * 成功
     */
    SUCCESS(20000, "操作成功"),
    /**
     * 未登录
     */
    NO_LOGIN(40001, "用户未登录"),
    /**
     * 没有操作权限
     */
    AUTHORIZED(40300, "没有操作权限"),
    /**
     * 系统异常
     */
    SYSTEM_ERROR(50000, "系统异常"),
    /**
     * 失败
     */
    FAIL(51000, "操作失败"),
    /**
     * 参数校验失败
     */
    VALID_ERROR(52000, "参数格式不正确");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String desc;

}