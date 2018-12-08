package com.kiwiframework.core.enums;

/**
 * 响应码枚举，参考HTTP状态码的语义
 * @author xiongzhao
 */
public enum ResultCode {
    //成功
    SUCCESS(0, "成功"),
    // 接口无权访问
    UNAUTHORIZED(301, "接口无权访问"),
    // 失败
    FAIL(400, "失败"),
    // 未认证
    TOKEN_EXPRIED(401, "未认证"),
    //接口不存在
    NOT_FOUND(404, "接口不存在"),
    //服务器内部错误
    INTERNAL_SERVER_ERROR(500, "系统错误");

    private final int code;

    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return code;
    }

    public String message() {
        return message;
    }
}
