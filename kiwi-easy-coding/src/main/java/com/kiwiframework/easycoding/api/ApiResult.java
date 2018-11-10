package com.kiwiframework.easycoding.api;

import com.alibaba.fastjson.JSON;
import com.kiwiframework.core.enums.ResultCode;

/**
 * 统一API响应结果封装
 * @author xiongzhao
 */
public class ApiResult<T> {
    private Integer code;
    private String message;
    private T data;

    public ApiResult setCode(Integer code) {
        this.code = code;
        return this;
    }

    public ApiResult setCode(ResultCode resultCode) {
        this.code = resultCode.code();
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ApiResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public ApiResult setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
