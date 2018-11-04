package com.kiwiframework.core.utils;

import com.kiwiframework.core.enums.ResultCode;
import com.kiwiframework.core.exception.AppException;
import org.springframework.util.StringUtils;

/**
 * 常用校验
 * @author xiongzhao1
 */
public class Checker {

    public static void notNull(Object value, String message) {
        if(value == null) {
            throw new AppException(ResultCode.FAIL, message);
        }
    }

    public static void notBlank(String value, String message) {
        if(value == null || StringUtils.isEmpty(value.trim())) {
            throw new AppException(ResultCode.FAIL, message);
        }
    }
}
