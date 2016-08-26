package com.koala.utils.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

//@Configuration
//@AutoConfigureAfter(MyBatisConfiguration.class)
public class MapperScannerConfiguration implements EnvironmentAware {

    Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);

    private RelaxedPropertyResolver propertyResolver = null;

    @Override
    public void setEnvironment(Environment environment) {
        propertyResolver = new RelaxedPropertyResolver(environment,"mybatis.");
    }

    @Bean(autowire = Autowire.BY_NAME)
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage(propertyResolver.getProperty("basePackage"));
        configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return configurer;
    }


}
