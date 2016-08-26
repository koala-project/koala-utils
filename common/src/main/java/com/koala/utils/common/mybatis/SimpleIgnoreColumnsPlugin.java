package com.koala.utils.common.mybatis;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.koala.utils.common.lang.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

/**
 * Created by Rocky on 15/12/14.
 */
public class SimpleIgnoreColumnsPlugin extends PluginAdapter {

    private String columns;
    private String superClass;

    private List<String> ignoreColumns;

    private String defaultPrimaryKey = "java.lang.Integer";
    private FullyQualifiedJavaType defaultPrimaryType = null;

    @Override
    public boolean validate(List<String> list) {
        columns = properties.getProperty("columns");
        superClass = properties.getProperty("superClass");

        if(StringUtils.isNotBlank(columns)){
            ignoreColumns =  Splitter.on(",").omitEmptyStrings().trimResults().splitToList(columns);
            return true;
        }
        return false;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {

        return ignoreColumn(field.getName());
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String fieldName = method.getName().replace("set","").toLowerCase();

        return ignoreColumn(fieldName);
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String fieldName = method.getName().replace("get","").toLowerCase();

        return ignoreColumn(fieldName);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType qualifiedJavaType = new FullyQualifiedJavaType(superClass);
        topLevelClass.setSuperClass(qualifiedJavaType);

        if(defaultPrimaryType == null){
            buildPrimaryKeyType(introspectedTable);
        }

        qualifiedJavaType.addTypeArgument(defaultPrimaryType);
        topLevelClass.addImportedType(qualifiedJavaType);

        return true;
    }

    @Override
    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {

        if(defaultPrimaryType == null){
            buildPrimaryKeyType(introspectedTable);
        }

        for(Element e:element.getElements()){
            XmlElement xmlElement = (XmlElement)e;
            if(xmlElement.getName().equals("id")){
                Attribute attribute = new Attribute("javaType",defaultPrimaryType.getShortName());
                xmlElement.addAttribute(attribute);
            }
        }

        return super.sqlMapResultMapWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement xmlElement = new XmlElement("selectKey");
        xmlElement.addAttribute(new Attribute("resultType",defaultPrimaryType.getFullyQualifiedName()));
        xmlElement.addAttribute(new Attribute("order","AFTER"));
        xmlElement.addAttribute(new Attribute("keyProperty","id"));
        xmlElement.addElement(new TextElement("SELECT LAST_INSERT_ID() AS ID"));
        element.addElement(0,xmlElement);
        return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
    }


    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        FullyQualifiedJavaType qualifiedJavaType = new FullyQualifiedJavaType("com.koala.base.entities.common.BaseCriteria");
        FullyQualifiedJavaType criterionJavaType = new FullyQualifiedJavaType("com.koala.base.entities.common.Criterion");

        topLevelClass.setSuperClass(qualifiedJavaType);
        topLevelClass.addImportedType(qualifiedJavaType);
        topLevelClass.addImportedType(criterionJavaType);

        topLevelClass.getInnerClasses().removeIf(innerClass -> innerClass.getType().getFullyQualifiedName().equals("Criterion"));
        topLevelClass.getFields().removeIf(field -> true);
        topLevelClass.getInnerClasses().forEach(innerClass -> {
            switch (innerClass.getType().getFullyQualifiedName()){
                case "GeneratedCriteria":
                    FullyQualifiedJavaType baseFullyQualifiedJavaType = new FullyQualifiedJavaType("com.koala.base.entities.common.BaseCriteria.InternelCriteria");
                    //baseFullyQualifiedJavaType.addTypeArgument(new FullyQualifiedJavaType("java.lang.Integer"));
                    innerClass.setSuperClass(baseFullyQualifiedJavaType);

                    innerClass.getMethods().removeIf(method -> {
                        if(ignoreColumns.stream().filter(column -> method.getName().replace("and","").toLowerCase().startsWith(column.toLowerCase())).count() > 0)
                            return true;
                        return false;
                    });

                    innerClass.getFields().removeIf(field -> field.getName().equals("criteria"));

                    break;
            }
        });
        return true;
    }

    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType("com.koala.base.repositories.common.SimpleMapper");
        type.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));

        if(defaultPrimaryType == null){
            buildPrimaryKeyType(introspectedTable);
        }

        type.addTypeArgument(defaultPrimaryType);
        interfaze.addSuperInterface(type);
        interfaze.getMethods().removeIf(m -> true);

        /**
         * 因为interfaze.getImportedTypes()返回的是一个unmodifiedSet，所以用反射把里面的值清空
         * */
        try {
            java.lang.reflect.Field field = interfaze.getClass().getDeclaredField("importedTypes");
            field.setAccessible(true);
            field.set(interfaze, Sets.newHashSet());
        } catch (Exception e) {
            e.printStackTrace();
        }

        interfaze.addImportedType(type);
        return false;
    }



    private boolean ignoreColumn(String fieldName){

        return ignoreColumns.stream().filter(ignoreColumn -> ignoreColumn.toLowerCase().equals(fieldName)).count() <= 0;

    }

    private void buildPrimaryKeyType(IntrospectedTable introspectedTable){
        List<IntrospectedColumn> columns = introspectedTable.getPrimaryKeyColumns();
        IntrospectedColumn column = null;
        if(columns != null && columns.size() > 0){
            column = columns.get(0);
        }
        if(column == null) {
            defaultPrimaryType = new FullyQualifiedJavaType(defaultPrimaryKey);
        }else{
            String javaType = "";
            switch (column.getJdbcTypeName().toLowerCase()){
                case "bigint":
                    javaType = "java.lang.Long";
                    break;
                case "int":
                    javaType = defaultPrimaryKey;
                    break;
                case "nvarchar":
                    javaType = "java.lang.String";
                    break;
                case "varchar":
                    javaType = "java.lang.String";
                    break;
                default:
                    javaType = defaultPrimaryKey;
                    break;
            }
            defaultPrimaryType = new FullyQualifiedJavaType(javaType);
        }
    }
}
