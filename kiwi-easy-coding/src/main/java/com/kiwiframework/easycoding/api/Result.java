package com.kiwiframework.easycoding.api;

import com.alibaba.fastjson.JSON;

/**
 * 统一API响应结果封装
 * @author xiongzhao
 */
public class Result<T> {
    private String code;
    private String message;
    private T data;

    public Result setCode(String code) {
        this.code = code;
        return this;
    }

    public Result setCode(ResultCode resultCode) {
        this.code = resultCode.code();
        return this;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Result setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
