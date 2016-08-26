package com.koala.utils.common.mybatis;

import com.google.common.collect.Lists;
import com.koala.utils.common.lang.StringUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Rocky on 15/12/18.
 */
public class LogicGeneratorPlugin extends PluginAdapter {

    Logger logger = Logger.getLogger(LogicGeneratorPlugin.class.getName());

    String target = null;
    String suffix = "Logic";
    String packageName = null;
    String superInterface = "com.koala.base.logics.common.CommonLogic";
    String superClass = "com.koala.base.logics.common.AbstractCommonLogicImpl";
    String primaryKeyClass = "java.lang.Integer";

    private static final String spliter = ".";

    @Override
    public boolean validate(List<String> warnings) {

        target = properties.getProperty("target");

        if(StringUtils.isBlank(target)){
            logger.log(Level.SEVERE,"target can not be null!");
            return false;
        }

        String suffixProperties = properties.getProperty("suffix");
        if(StringUtils.isBlank(suffixProperties))
            logger.info("suffix is null,use the default value 'Logic' to continue.");
        else
            suffix = suffixProperties;


        packageName = properties.getProperty("packageName");
        if(StringUtils.isBlank(packageName))
            logger.info("packageName is null,use the default value 'logic' to automatic build.");

        String superInterfaceProperties = properties.getProperty("superInterface");
        if(StringUtils.isBlank(superInterfaceProperties))
            logger.info("superInterface is null,use the default value 'com.koala.base.logics.common.CommonLogic' to continue.");
        else
            superInterface = superInterfaceProperties;

        String superClassProperties = properties.getProperty("superClass");
        if(StringUtils.isBlank(superClassProperties))
            logger.info("superClass is null,use the default value 'com.koala.base.logics.common.AbstractCommonLogicImpl' to continue.");
        else
            superClass = superClassProperties;

        String primaryKeyClassProperties = properties.getProperty("primaryKeyClass");
        if(StringUtils.isBlank(primaryKeyClassProperties))
            logger.info("primaryKeyClass is null,use the default value 'java.lang.Integer' to continue.");
        else
            primaryKeyClass = primaryKeyClassProperties;


        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {

        FullyQualifiedJavaType mapperType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        FullyQualifiedJavaType primaryKeyType = new FullyQualifiedJavaType(primaryKeyClass);

        DefaultJavaFormatter javaFormatter = new DefaultJavaFormatter();


        String modelName = modelType.getShortName();
        String logicName = packageName + spliter + modelName + suffix;

        // Generate Logic Interface

        Interface interfaze = new Interface(new FullyQualifiedJavaType(logicName));

        interfaze.setVisibility(JavaVisibility.PUBLIC);

        FullyQualifiedJavaType superInterfaceType = new FullyQualifiedJavaType(superInterface);

        superInterfaceType.addTypeArgument(modelType);
        superInterfaceType.addTypeArgument(primaryKeyType);
        interfaze.addSuperInterface(superInterfaceType);


        interfaze.addImportedType(superInterfaceType);
        interfaze.addImportedType(modelType);


        GeneratedJavaFile logicFile = new GeneratedJavaFile(interfaze, target,javaFormatter);


        //Generate logic Implementation

        FullyQualifiedJavaType superClassType = new FullyQualifiedJavaType(superClass);

        String logicImplName = packageName + spliter + modelName + suffix + "Impl";


        TopLevelClass topLevelClass = new TopLevelClass(new FullyQualifiedJavaType(logicImplName));
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addSuperInterface(interfaze.getType());


        superClassType.addTypeArgument(mapperType);
        superClassType.addTypeArgument(modelType);
        superClassType.addTypeArgument(primaryKeyType);

        topLevelClass.setSuperClass(superClassType);

        topLevelClass.addImportedType(modelType);
        topLevelClass.addImportedType(mapperType);
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Component"));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("javax.annotation.Resource"));
        topLevelClass.addImportedType(superClassType);

        topLevelClass.addAnnotation("@Component");

        String fieldName = StringUtils.firstLowerCase(mapperType.getShortName());

        Field field = new Field(fieldName,mapperType);
        field.addAnnotation("\n\t@Resource");


        topLevelClass.addField(field);


        Method method = new Method("getMapper");
        method.setReturnType(mapperType);
        method.addBodyLine(String.format("return %s;", fieldName));
        method.setVisibility(JavaVisibility.PROTECTED);

        topLevelClass.addMethod(method);

        GeneratedJavaFile logicImplFile = new GeneratedJavaFile(topLevelClass,target,javaFormatter);

        return Lists.newArrayList(logicFile, logicImplFile);
    }
}
