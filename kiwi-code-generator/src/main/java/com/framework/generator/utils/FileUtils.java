package com.framework.generator.utils;

import java.io.*;

/**
 * 文件工具类
 * @author xiongzhao
 */
public class FileUtils {

    /**
     * 获取文件流
     * @param resource
     * @return
     * @throws Exception
     */
    public static InputStream getFileStream(String resource) throws Exception{

        FileInputStream stream = new FileInputStream(resource);

        return stream;
    }

    /**
     * 判断文件/目录是否存在
     * @param filePath
     * @return
     */
    public static boolean isExists(String filePath){
        return new File(filePath).exists();
    }
}
