package com.koala.utils.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Administrator on 2016-04-21.
 */
@Configuration    //该注解类似于spring配置文件
@MapperScan(basePackages="com.koala.base.repositories")
@ConditionalOnClass({ EnableTransactionManagement.class})
public class MyBatisConfigurationSingle implements EnvironmentAware {

    private Logger logger = Logger.getLogger(MyBatisConfiguration.class);

    private RelaxedPropertyResolver relaxedPropertyResolver;

    @Override
    public void setEnvironment(Environment environment) {
        this.relaxedPropertyResolver = new RelaxedPropertyResolver(environment, "mybatis.");
    }

    @Bean
    public DataSource defaultDataSource(){
        logger.info("Use the defaultdataSource");
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(relaxedPropertyResolver.getProperty("url"));
        dataSource.setDriverClassName(relaxedPropertyResolver.getProperty("driverClassName"));
        dataSource.setUsername(relaxedPropertyResolver.getProperty("username"));
        dataSource.setPassword(relaxedPropertyResolver.getProperty("password"));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(defaultDataSource());
    }


    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(defaultDataSource());
    }

    @Bean
    public PageHelper pageHelper(){
        logger.info("MyBatis分页插件PageHelper");
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }

    @Bean(name = "sqlSessionFactory")
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory() {
        try {
            SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
            sessionFactory.setDataSource(defaultDataSource());//指定数据源(这个必须有，否则报错)
            sessionFactory.setTypeAliasesPackage(relaxedPropertyResolver.getProperty("typeAliasesPackage"));//映射的实体包
            sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(relaxedPropertyResolver.getProperty("mapperLocations")));//Mapper文件
            sessionFactory.setPlugins(new Interceptor[]{pageHelper()});
            return sessionFactory.getObject();
        } catch (Exception e) {
            logger.warn("Could not confiure mybatis session factory",e);
            return null;
        }
    }

}
