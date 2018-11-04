package com.kiwiframework.core.exception;

import com.kiwiframework.core.enums.ResultCode;

/**
 * 服务（业务）异常如“ 账号或密码错误 ”，该异常只做INFO级别的日志记录
 * @author xiongzhao
 */
public class AppException extends RuntimeException {

    private String code;

    public AppException() {
    }

    public AppException(String message) {
        super(message);
        this.code = ResultCode.FAIL.code();
    }

    public AppException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.code();
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
        this.code = ResultCode.FAIL.code();
    }

    public AppException(ResultCode resultCode, String message, Throwable cause) {
        super(message, cause);
        this.code = resultCode.code();
    }
}
