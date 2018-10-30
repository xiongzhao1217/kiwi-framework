package com.kiwiframework.generator.conf;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 覆盖数据库类型与java类型映射
 * @author xiongzhao
 */
public class JavaTypeResolverImpl extends JavaTypeResolverDefaultImpl {

    public JavaTypeResolverImpl() {
        super();
        // 读取配置文件中的类型映射配置，并进行覆盖
        overrideDataType();
    }

    /**
     * 数据库typeNameMap
     */
    private static HashMap<String, Integer> typeNameMap = new HashMap<String, Integer>(){{
            put("BIT", Types.BIT);
            put("TINYINT", Types.TINYINT);
            put("SMALLINT", Types.SMALLINT);
            put("INTEGER", Types.INTEGER);
            put("BIGINT", Types.BIGINT);
            put("FLOAT", Types.FLOAT);
            put("REAL", Types.REAL);
            put("DOUBLE", Types.DOUBLE);
            put("NUMERIC", Types.NUMERIC);
            put("DECIMAL", Types.DECIMAL);
            put("CHAR", Types.CHAR);
            put("VARCHAR", Types.VARCHAR);
            put("LONGVARCHAR", Types.LONGVARCHAR);
            put("DATE", Types.DATE);
            put("TIME", Types.TIME);
            put("TIMESTAMP", Types.TIMESTAMP);
            put("BINARY", Types.BINARY);
            put("VARBINARY", Types.VARBINARY);
            put("LONGVARBINARY", Types.LONGVARBINARY);
            put("NULL", Types.NULL);
            put("OTHER", Types.OTHER);
            put("JAVA_OBJECT", Types.JAVA_OBJECT);
            put("DISTINCT", Types.DISTINCT);
            put("STRUCT", Types.STRUCT);
            put("ARRAY", Types.ARRAY);
            put("BLOB", Types.BLOB);
            put("CLOB", Types.CLOB);
            put("REF", Types.REF);
            put("DATALINK", Types.DATALINK);
            put("BOOLEAN", Types.BOOLEAN);
            put("ROWID", Types.ROWID);
            put("NCHAR", Types.NCHAR);
            put("NVARCHAR", Types.NVARCHAR);
            put("LONGNVARCHAR", Types.LONGNVARCHAR);
            put("NCLOB", Types.NCLOB);
            put("SQLXML", Types.SQLXML);
    }};

    /**
     * 覆盖数据库类型与java类型的默认映射
     */
    private void overrideDataType() {
        if (Config.DB_JAVA_TYPE_MAP != null && Config.DB_JAVA_TYPE_MAP.size() > 0){
            Iterator<String> it = Config.DB_JAVA_TYPE_MAP.keySet().iterator();
            while (it.hasNext()){
                String dbType = it.next();
                String javaType = Config.DB_JAVA_TYPE_MAP.get(dbType);
                dbType = dbType.toUpperCase();
                super.typeMap.put(typeNameMap.get(dbType), new JdbcTypeInformation(dbType, new FullyQualifiedJavaType(javaType)));
            }
        }
    }

}