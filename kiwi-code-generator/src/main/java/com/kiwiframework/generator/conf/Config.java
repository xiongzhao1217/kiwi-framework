package com.kiwiframework.generator.conf;

import com.alibaba.fastjson.JSON;
import com.kiwiframework.generator.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 代码生成工具配置类
 * @author xiongzhao
 */
public class Config {

    private static Logger logger = LoggerFactory.getLogger(Config.class);

    /**
     * 项目模块前缀
     */
    public static String PROJECT_MODULE_PREFIX = "kiwi-demo";
    /**
     * 包路径
     */
    public static String BASE_PACKAGE = "com.kiwiframework.demo";
    /**
     * 业务模块路径
     */
    public static String BIZ_MODULE_PATH = "";

    /**
     * 子模块名称
     */
    public static String TARGET_MODULE_NAME = "";

    /**
     * 注释中的author
     */
    public static String AUTHOR = "CodeGenerator";
    /**
     * JDBC配置
     */
    public static String JDBC_URL = "";
    public static String JDBC_USERNAME = "";
    public static String JDBC_PASSWORD = "";
    public static String JDBC_DIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    /**
     * 项目在硬盘上的基础路径
     */
    public static String PROJECT_PATH = System.getProperty("user.dir");
    /**
     * 模板位置
     */
    public static String TEMPLATE_FILE_PATH =  "/src/test/resources/template";
    /**
     * java文件路径
     */
    public static String JAVA_PATH = "/src/main/java";
    /**
     * 资源文件路径
     */
    public static String RESOURCES_PATH = "/src/main/resources";

    /**
     * model存放的路径名称
     */
    public static String MODEL_PACKAGE_NAME = "model";

    /**
     * Model所在包
     */
    public static String MODEL_PACKAGE = BASE_PACKAGE + "." + MODEL_PACKAGE_NAME;
    /**
     * Mapper所在包
     */
    public static String MAPPER_PACKAGE = BASE_PACKAGE + ".dao";
    /**
     * Service所在包
     */
    public static String SERVICE_PACKAGE = BASE_PACKAGE + ".service";
    /**
     * Controller所在包
     */
    public static String CONTROLLER_PACKAGE;

    /**
     * 默认控制器路径
     */
    public static String DEFAULT_BASE_CONTROLLER = ".controller";

    /**
     * mapper.xml文件存放路径
     */
    public static String BASE_MAPPER_XML_PATH = "mapper";

    /**
     * Mapper插件基础接口的完全限定名
     */
    public static String MAPPER_INTERFACE_REFERENCE = "com.kiwiframework.easycoding.base.BaseMapper";

    /**
     * 是否使用lombok
     */
    public static boolean MODEL_USE_LOMBOK = false;

    public static String PACKAGE_SUFFIX_DOMIAN = "";
    public static String PACKAGE_SUFFIX_DAO = "";
    public static String PACKAGE_SUFFIX_SERVICE = "";
    public static String PACKAGE_SUFFIX_WEB = "";

    /**
     * 数据库类型与java类型映射map，用于覆盖默认映射
     */
    public static Map<String, String> DB_JAVA_TYPE_MAP = null;

    static{
        Properties pro = new Properties();
        try{
            String path = Config.class.getResource("/").getPath() + "code_generator.properties";
            logger.info("配置文件路径:" + path);
            InputStream is = FileUtils.getFileStream(path);
            pro.load(is);
            logger.info("配置文件内容：" + pro);
        }catch(Exception e){
            e.printStackTrace();
        }
        init(pro);
        if(TARGET_MODULE_NAME != null) {
            // 项目的dao、service、controller在同一个模块下
            PROJECT_PATH = PROJECT_PATH + "/" + TARGET_MODULE_NAME;
        }else {
            PROJECT_PATH = PROJECT_PATH + "/" + PROJECT_MODULE_PREFIX;
            PACKAGE_SUFFIX_DOMIAN = "-domian";
            PACKAGE_SUFFIX_DAO = "-dao";
            PACKAGE_SUFFIX_SERVICE = "-service";
            PACKAGE_SUFFIX_WEB = "-web";
        }
    }

    public static void init(Properties pro){
        int index = PROJECT_PATH.lastIndexOf("/");
        if(index == -1) {
            index = PROJECT_PATH.lastIndexOf("\\");
        }
        AUTHOR = pro.getProperty("doc.author")==null?AUTHOR:pro.getProperty("doc.author");
        String projectName = PROJECT_PATH.substring(index);
        PROJECT_MODULE_PREFIX = pro.getProperty("project.module.prefix")==null?projectName:pro.getProperty("project.module.prefix");
        BASE_PACKAGE = pro.getProperty("base.package")==null?BASE_PACKAGE:pro.getProperty("base.package");
        BIZ_MODULE_PATH = pro.getProperty("biz.module.path")==null?BIZ_MODULE_PATH:prefixByString(".", pro.getProperty("biz.module.path"));
        TARGET_MODULE_NAME = pro.getProperty("target.module.name")==null?TARGET_MODULE_NAME:pro.getProperty("target.module.name");
        JDBC_URL = pro.getProperty("jdbc.url")==null?JDBC_URL:pro.getProperty("jdbc.url");
        JDBC_USERNAME = pro.getProperty("jdbc.username")==null?JDBC_USERNAME:pro.getProperty("jdbc.username");
        JDBC_PASSWORD = pro.getProperty("jdbc.password")==null?JDBC_PASSWORD:pro.getProperty("jdbc.password");
        JDBC_DIVER_CLASS_NAME = pro.getProperty("jdbc.diver.class.name")==null?JDBC_DIVER_CLASS_NAME:pro.getProperty("jdbc.diver.class.name");
        TEMPLATE_FILE_PATH = Config.class.getClassLoader().getResource("template").getPath();
        MODEL_PACKAGE_NAME = pro.getProperty("model.package.name")==null?MODEL_PACKAGE_NAME:pro.getProperty("model.package.name");
        MODEL_PACKAGE = BASE_PACKAGE + "." + MODEL_PACKAGE_NAME;
        MAPPER_PACKAGE = BASE_PACKAGE + ".dao";
        SERVICE_PACKAGE = BASE_PACKAGE + ".service";
        DEFAULT_BASE_CONTROLLER = pro.getProperty("base.controller")==null?DEFAULT_BASE_CONTROLLER:prefixByString("." ,pro.getProperty("base.controller"));
        CONTROLLER_PACKAGE = BASE_PACKAGE + DEFAULT_BASE_CONTROLLER;
        BASE_MAPPER_XML_PATH = pro.getProperty("base.mapper.xml.path")==null?BASE_MAPPER_XML_PATH:pro.getProperty("base.mapper.xml.path");
        DB_JAVA_TYPE_MAP = StringUtils.isEmpty(pro.getProperty("db.java.type.map"))?null:JSON.parseObject(pro.getProperty("db.java.type.map"), Map.class);
        MAPPER_INTERFACE_REFERENCE = StringUtils.isEmpty(pro.getProperty("mapper.super.interface"))?MAPPER_INTERFACE_REFERENCE:pro.getProperty("mapper.super.interface");
        MODEL_USE_LOMBOK = StringUtils.isEmpty(pro.getProperty("model.use.lombok"))? MODEL_USE_LOMBOK : pro.getProperty("model.use.lombok").equals("true");
    }

    /**
     * 将目标字符串转为指定字符串开头的新字符串
     * 如果已经按照指定字符串开头了，返回原字符串，否则拼接前缀后返回
     * @param prefix
     * @param s
     * @return
     */
    public static String prefixByString(String prefix, String s){
        if(StringUtils.isEmpty(s)) {
            return s;
        }
        if(s.indexOf(prefix) != 0){
            s = prefix + s;
        }
        return s;
    }
}
