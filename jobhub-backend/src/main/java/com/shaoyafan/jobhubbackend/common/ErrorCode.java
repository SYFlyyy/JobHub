package com.shaoyafan.jobhubbackend.common;

/**
 * 自定义错误码
 *
 * @author SYF
 */
public enum ErrorCode {

    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录或登录已过期"),
    NO_AUTH_ERROR(40200, "无权限"),
    STATE_ERROR(40300, "状态异常"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    TOO_MANY_REQUEST(40500, "请求过于频繁"),
    FORBIDDEN_ERROR(40600, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }
}
