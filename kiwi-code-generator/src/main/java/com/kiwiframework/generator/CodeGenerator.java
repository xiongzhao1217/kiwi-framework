package com.kiwiframework.generator;

import com.kiwiframework.generator.conf.Config;
import com.google.common.base.CaseFormat;
import com.kiwiframework.generator.utils.FileUtils;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 代码生成器，根据数据表名称生成对应的Model、Mapper、Service、Controller简化开发。
 * @author xiongzhao
 */
public class CodeGenerator {

    private static Logger logger = LoggerFactory.getLogger(CodeGenerator.class);

    /**
     * 生成的Service存放路径
     */
    private static final String PACKAGE_PATH_SERVICE = packageConvertPath(Config.SERVICE_PACKAGE + Config.BIZ_MODULE_PATH);
    /**
     * 生成的Service实现存放路径
     */
    private static final String PACKAGE_PATH_SERVICE_IMPL = packageConvertPath(Config.SERVICE_PACKAGE + Config.BIZ_MODULE_PATH + ".impl");
    /**
     * 生成的Controller存放路径
     */
    private static final String PACKAGE_PATH_CONTROLLER = packageConvertPath(Config.CONTROLLER_PACKAGE + Config.BIZ_MODULE_PATH);
    /**
     * @date
     */
    private static final String DATE = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

    public static void main(String[] args) {
//        genCode("t_user");
        genCodeByCustomModelName("t_user","User");
//        genCodeByCustomModelName("t_user","User");
//        genModelAndMapper("t_user","User");
//        genService("t_user","User");
//        genController("t_user","User");
    }

    /**
     * 生成dao、service和controller代码
     * 按照数据库表名转驼峰命名文件名
     * 支持批量操作
     * @param tableNames 数据表名称...
     */
    public static void genDefaultCode(String... tableNames) {
        for (String tableName : tableNames) {
            genCodeByCustomModelName(tableName, null);
        }
    }

    /**
     * 生成dao、service和controller代码
     * 指定生成的文件名称
     * @param tableName
     * @param modelName
     */
    public static void genCode(String tableName, String modelName) {
            genCodeByCustomModelName(tableName, modelName);
    }

    /**
     * 生成dao和Service代码
     * 按照数据库表名转驼峰命名文件名
     * 支持批量生成
     * @param tableNames 数据表名称...
     */
    public static void genDefaultDaoAndService(String... tableNames) {
        for (String tableName : tableNames) {
            genModelAndMapper(tableName, null);
            genService(tableName, null);
        }
    }

    /**
     * 生成dao和Service代码
     * 指定生成的文件名称
     */
    public static void genDaoAndService(String tableName, String modelName) {
        genModelAndMapper(tableName, modelName);
        genService(tableName, modelName);
    }

    /**
     * 通过数据表名称，和自定义的 Model 名称生成代码
     * 如输入表名称 "t_user_detail" 和自定义的 Model 名称 "User" 将生成 User、UserMapper、UserService ...
     * @param tableName 数据表名称
     * @param modelName 自定义的 Model 名称
     */
    private static void genCodeByCustomModelName(String tableName, String modelName) {
        genModelAndMapper(tableName, modelName);
        genService(tableName, modelName);
        genController(tableName, modelName);
    }


    public static void genModelAndMapper(String tableName, String modelName) {
        Context context = new Context(ModelType.FLAT);
        context.setId("Potato");
        context.setTargetRuntime("MyBatis3Simple");
        context.addProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER, "`");
        context.addProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER, "`");

        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setConnectionURL(Config.JDBC_URL);
        jdbcConnectionConfiguration.setUserId(Config.JDBC_USERNAME);
        jdbcConnectionConfiguration.setPassword(Config.JDBC_PASSWORD);
        jdbcConnectionConfiguration.setDriverClass(Config.JDBC_DIVER_CLASS_NAME);
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);
        // 插件配置
        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType("com.kiwiframework.generator.conf.DefaultMapperPlugin");
        pluginConfiguration.addProperty("mappers", Config.MAPPER_INTERFACE_REFERENCE);
        if (Config.MODEL_USE_LOMBOK) {
            pluginConfiguration.addProperty("lombok", "Data,Builder");
            pluginConfiguration.addProperty("generateColumnConsts", "true");
        }
        context.addPluginConfiguration(pluginConfiguration);

        // model配置
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        String modelPath = Config.PROJECT_PATH + Config.PACKAGE_SUFFIX_DOMIAN + Config.JAVA_PATH;
        File modelDir = new File(modelPath);
        if(!modelDir.exists()) {
            modelDir.mkdirs();
        }
        javaModelGeneratorConfiguration.setTargetProject(modelPath);
        javaModelGeneratorConfiguration.setTargetPackage(Config.MODEL_PACKAGE + Config.BIZ_MODULE_PATH);
        javaModelGeneratorConfiguration.addProperty("rootClass", "com.kiwiframework.easycoding.base.BaseModel");
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

        // mapper xml配置
        String xmlPath = Config.PROJECT_PATH + Config.PACKAGE_SUFFIX_DAO + Config.RESOURCES_PATH;
        File xmlDir = new File(xmlPath);
        if(!xmlDir.exists()) {
            xmlDir.mkdirs();
        }
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetProject(xmlPath);
        sqlMapGeneratorConfiguration.setTargetPackage(Config.BASE_MAPPER_XML_PATH + Config.BIZ_MODULE_PATH);
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

        // mapper接口配置
        String interfacePath = Config.PROJECT_PATH + Config.PACKAGE_SUFFIX_DAO + Config.JAVA_PATH;
        File mapperDir = new File(interfacePath);
        if(!mapperDir.exists()) {
            mapperDir.mkdirs();
        }
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetProject(Config.PROJECT_PATH + Config.PACKAGE_SUFFIX_DAO + Config.JAVA_PATH);
        javaClientGeneratorConfiguration.setTargetPackage(Config.MAPPER_PACKAGE + Config.BIZ_MODULE_PATH);
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

        // 数据库字段与java字段类型映射
        JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration();
        javaTypeResolverConfiguration.setConfigurationType("com.kiwiframework.generator.conf.JavaTypeResolverImpl");
        context.setJavaTypeResolverConfiguration(javaTypeResolverConfiguration);

        TableConfiguration tableConfiguration = new TableConfiguration(context);
        tableConfiguration.setTableName(tableName);

        if (StringUtils.isNotEmpty(modelName)) {
            tableConfiguration.setDomainObjectName(modelName);
        }
        tableConfiguration.setGeneratedKey(new GeneratedKey("id", "Mysql", true, null));
        context.addTableConfiguration(tableConfiguration);

        List<String> warnings;
        MyBatisGenerator generator;
        try {
            Configuration config = new Configuration();
            config.addContext(context);
            config.validate();

            boolean overwrite = true;
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            warnings = new ArrayList<String>();
            generator = new MyBatisGenerator(config, callback, warnings);
            generator.generate(null);
        } catch (Exception e) {
            throw new RuntimeException("生成Model和Mapper失败", e);
        }

        if (generator.getGeneratedJavaFiles().isEmpty() || generator.getGeneratedXmlFiles().isEmpty()) {
            throw new RuntimeException("生成Model和Mapper失败：" + warnings);
        }
        if (StringUtils.isEmpty(modelName)) {
            modelName = tableNameConvertUpperCamel(tableName);
        }
        logger.info(Config.MODEL_PACKAGE + Config.BIZ_MODULE_PATH + "." + modelName + ".java 生成成功");
        logger.info(Config.MAPPER_PACKAGE + Config.BIZ_MODULE_PATH + "." + modelName + "Mapper.java 生成成功");
        logger.info(modelName + "Mapper.xml 生成成功");
    }

    public static void genService(String tableName, String modelName) {
        try {
            freemarker.template.Configuration cfg = getConfiguration("/service.ftl");

            Map<String, Object> data = new HashMap<>();
            data.put("date", DATE);
            data.put("author", Config.AUTHOR);
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
            data.put("basePackage", Config.BASE_PACKAGE);
            data.put("modulePath", Config.BIZ_MODULE_PATH);
            data.put("modulePath", Config.BIZ_MODULE_PATH);
            data.put("modelPackageName", Config.MODEL_PACKAGE_NAME);

            File file = new File(Config.PROJECT_PATH + Config.PACKAGE_SUFFIX_SERVICE + Config.JAVA_PATH + PACKAGE_PATH_SERVICE + modelNameUpperCamel + "Service.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("service.ftl").process(data,
                    new FileWriter(file));
            logger.info(modelNameUpperCamel + "Service.java 生成成功");

            File file1 = new File(Config.PROJECT_PATH+ Config.PACKAGE_SUFFIX_SERVICE + Config.JAVA_PATH + PACKAGE_PATH_SERVICE_IMPL + modelNameUpperCamel + "ServiceImpl.java");
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();
            }
            cfg = getConfiguration("/service-impl.ftl");
            cfg.getTemplate("service-impl.ftl").process(data,
                    new FileWriter(file1));
            logger.info(modelNameUpperCamel + "ServiceImpl.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Service失败" + e.getMessage(), e);
        }
    }

    public static void genController(String tableName, String modelName) {
        try {
            freemarker.template.Configuration cfg = getConfiguration("/controller.ftl");

            Map<String, Object> data = new HashMap<>();
            data.put("date", DATE);
            data.put("author", Config.AUTHOR);
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
            data.put("baseRequestMapping", modelNameConvertMappingPath(modelNameUpperCamel));
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
            data.put("basePackage", Config.BASE_PACKAGE);
            data.put("modulePath", Config.BIZ_MODULE_PATH);
            data.put("baseController", Config.DEFAULT_BASE_CONTROLLER);


            File file = new File(Config.PROJECT_PATH + Config.PACKAGE_SUFFIX_WEB  + Config.JAVA_PATH + PACKAGE_PATH_CONTROLLER + modelNameUpperCamel + "Controller.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            //cfg.getTemplate("controller-restful.ftl").process(data, new FileWriter(file));
            cfg.getTemplate("controller.ftl").process(data, new FileWriter(file));

            logger.info(modelNameUpperCamel + "Controller.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Controller失败" + e.getMessage(), e);
        }

    }

    private static freemarker.template.Configuration getConfiguration(String fileName) throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        // 支持外部模板覆盖内部模板，优先取外面的模板，外面没有再取内部的默认模板
        if(FileUtils.isExists(Config.TEMPLATE_FILE_PATH + fileName)) {
            cfg.setDirectoryForTemplateLoading(new File(Config.TEMPLATE_FILE_PATH));
        }else {
            cfg.setClassLoaderForTemplateLoading(CodeGenerator.class.getClassLoader(),  "/template/");
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }

    private static String tableNameConvertLowerCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.toLowerCase());
    }

    private static String tableNameConvertUpperCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toLowerCase());
    }

    private static String tableNameConvertMappingPath(String tableName) {
        //兼容使用大写的表名
        tableName = tableName.toLowerCase();
        return "/" + (tableName.contains("_") ? tableName.replaceAll("_", "/") : tableName);
    }

    private static String modelNameConvertMappingPath(String modelName) {
        String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelName);
        return tableNameConvertMappingPath(tableName);
    }

    private static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

}
