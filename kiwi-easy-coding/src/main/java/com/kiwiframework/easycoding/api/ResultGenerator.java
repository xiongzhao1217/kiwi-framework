package com.kiwiframework.easycoding.api;

import com.kiwiframework.core.enums.ResultCode;

/**
 * 响应结果生成工具
 * @author xiongzhao
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public static ApiResult success() {
        return new ApiResult()
                .setCode(ResultCode.SUCCESS)
                .setMessage(ResultCode.SUCCESS.message());
    }

    public static <T> ApiResult success(T data) {
        return new ApiResult()
                .setCode(ResultCode.SUCCESS)
                .setMessage(ResultCode.SUCCESS.message())
                .setData(data);
    }

    public static ApiResult error(String message) {
        return new ApiResult()
                .setCode(ResultCode.FAIL)
                .setMessage(message);
    }

    public static ApiResult error(ResultCode resultCode) {
        return new ApiResult()
                .setCode(resultCode.code())
                .setMessage(resultCode.message());
    }

    public static ApiResult error(Integer code, String message) {
        return new ApiResult()
                .setCode(code)
                .setMessage(message);
    }

    public static ApiResult error(ResultCode resultCode, String message) {
        return new ApiResult()
                .setCode(resultCode.code())
                .setMessage(message);
    }
}
