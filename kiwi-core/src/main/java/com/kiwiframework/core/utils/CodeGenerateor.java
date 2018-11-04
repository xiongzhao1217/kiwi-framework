package com.kiwiframework.core.utils;


import java.util.UUID;

/**
 * @author xiongzhao1
 */
public class CodeGenerateor {

    /**
     * 生成uuid
     * @return
     */
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }

}
