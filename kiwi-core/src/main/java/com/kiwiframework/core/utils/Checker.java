package com.kiwiframework.core.utils;

import com.kiwiframework.core.enums.ResultCode;
import com.kiwiframework.core.exception.AppException;
import org.springframework.util.StringUtils;

/**
 * 常用校验
 * @author xiongzhao1
 */
public class Checker {

    /**
     * 非空校验
     * @param value 需要校验的对象
     * @param message 错误提示信息
     */
    public static void notNull(Object value, String message) {
        if(value == null) {
            throw new AppException(ResultCode.FAIL, message);
        }
    }

    /**
     * 字符串非空校验
     * null or '' is false
     * @param value 需要校验的字符串
     * @param message 错误提示信息
     */
    public static void notBlank(String value, String message) {
        if(value == null || StringUtils.isEmpty(value.trim())) {
            throw new AppException(ResultCode.FAIL, message);
        }
    }
}
