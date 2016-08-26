package com.koala.utils.config;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {

    @Bean
    public DozerBeanMapper dozerMapper(){
        return new DozerBeanMapper();
    }
}
