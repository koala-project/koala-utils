package com.koala.utils.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

//@Configuration
//@EnableTransactionManagement
public class DataSourceConfiguration implements EnvironmentAware{

    Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);

    private RelaxedPropertyResolver propertyResolver = null;


    @Override
    public void setEnvironment(Environment environment) {
        propertyResolver = new RelaxedPropertyResolver(environment,"jdbc.");
    }

    @Bean(name = "dataSource",destroyMethod = "close",initMethod = "init")
    public DataSource dataSource(){

        logger.debug("Initialization DataSource");

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(propertyResolver.getProperty("url"));
        dataSource.setUsername(propertyResolver.getProperty("user"));
        dataSource.setPassword(propertyResolver.getProperty("password"));

        return dataSource;
    }
}
