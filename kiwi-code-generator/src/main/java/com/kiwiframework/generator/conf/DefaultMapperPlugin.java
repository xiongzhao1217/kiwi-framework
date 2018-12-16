package com.kiwiframework.generator.conf;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;
import tk.mybatis.mapper.generator.MapperPlugin;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;


/**
 * 自定义mapper xml生成内容
 * @author xiongzhao
 */
public class DefaultMapperPlugin extends tk.mybatis.mapper.generator.MapperPlugin {

    /**
     * 是否生成lombok的@Data注解
     */
    private boolean needsData = false;

    /**
     * 是否生成lombok的@Bulider注解
     */
    private boolean needsBuilder = false;

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        this.generateSqlBaseColumns(document, introspectedTable);
        return true;
    }

    /**
     * mapper.xml生成Base_Column_List
     * @param document
     * @param introspectedTable
     */
    private void generateSqlBaseColumns(Document document, IntrospectedTable introspectedTable) {
        XmlElement rootElement = document.getRootElement();
        XmlElement sqlElement = new XmlElement("sql");
        Attribute attr = new Attribute("id", "Base_Column_List");
        sqlElement.addAttribute(attr);
        TextElement comment = new TextElement("<!-- WARNING - @mbg.generated -->");
        StringBuilder columnsBuilder = new StringBuilder();
        List columnList = introspectedTable.getAllColumns();
        Iterator it = columnList.iterator();
        int i = 1;
        while(it.hasNext()) {
            IntrospectedColumn content = (IntrospectedColumn)it.next();
            columnsBuilder.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime() + '.' + content.getActualColumnName()).append(", ");
            if (i++ % 6 == 0) {
                columnsBuilder.append("\n\t");
            }
        }

        String columns = columnsBuilder.substring(0, columnsBuilder.length() - 2);
        TextElement content = new TextElement(columns);
        sqlElement.addElement(comment);
        sqlElement.addElement(content);
        rootElement.addElement(new TextElement(""));
        rootElement.addElement(sqlElement);
        rootElement.addElement(new TextElement(""));
    }

    /**
     * 获取lombok配置
     * @param properties
     */
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        String lombok = this.getProperty("lombok");
        if(StringUtils.isNotEmpty(lombok)) {
            this.needsData = lombok.contains("Data");
            this.needsBuilder = lombok.contains("Builder");
        }
    }

    /**
     * 是否生成getter方法
     * @return
     */
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return super.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, modelClassType) && !needsData;
    }

    /**
     * 是否生成setter方法
     * @return
     */
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return super.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, modelClassType) && !needsData;
    }

    /**
     * 处理实体类
     * @param topLevelClass
     * @param introspectedTable
     */
    private void processEntityClass(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if(this.needsData) {
            topLevelClass.addImportedType("lombok.Data");
            topLevelClass.addAnnotation("@Data");
        }

        if(this.needsBuilder) {
            topLevelClass.addImportedType("lombok.Builder");
            topLevelClass.addAnnotation("@Builder");
            // 使用@Bulider注解,顺带生成@Tolerate的构造方法
            topLevelClass.addImportedType("lombok.experimental.Tolerate");
            Method method = new Method();
            method.addAnnotation("@Tolerate");
            method.setConstructor(true);
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addBodyLine("");
            method.setName(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
            topLevelClass.addMethod(method);
        }

        // BaseModel已经用创建时间和更新时间常量,需要删除createTime和updateTime字段常量
        Iterator<Field> it = topLevelClass.getFields().iterator();
        while (it.hasNext()) {
            Field field = it.next();
            if ("CREATE_TIME".equals(field.getName()) || "UPDATE_TIME".equals(field.getName())) {
                it.remove();
            }
        }
    }

    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
        this.processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        super.modelPrimaryKeyClassGenerated(topLevelClass, introspectedTable);
        this.processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.modelRecordWithBLOBsClassGenerated(topLevelClass, introspectedTable);
        this.processEntityClass(topLevelClass, introspectedTable);
        return false;
    }
}
