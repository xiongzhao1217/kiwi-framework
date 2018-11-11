package com.kiwiframework.core.utils;


import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

/**
 * @author xiongzhao1
 */
public class CodeGenerateor {

    private static final char[] randomChars = new char[]{'a','b','c','d','e','f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
            'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

    /**
     * 生成uuid
     * @return
     */
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * 随机生成字符串
     * @param count 字符串长度
     * @return
     */
    public static String random(int count) {
        return RandomStringUtils.random(count, randomChars);
    }

}
