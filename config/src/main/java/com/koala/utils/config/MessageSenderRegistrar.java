package com.koala.utils.config;

import com.koala.utils.config.annotation.EnableJMSSender;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Created by Rocky on 16/3/2.
 */
public class MessageSenderRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableJMSSender.class.getName()));
        String queue = attributes.getString("queue");
        if(StringUtils.isBlank(queue)){
            throw new RuntimeException("queue 属性不能为空");
        }

        BeanDefinitionBuilder senderBuilder = BeanDefinitionBuilder.genericBeanDefinition(MessageSender.class);
        senderBuilder.addConstructorArgReference("jmsTemplate").addConstructorArgValue(queue);


        beanDefinitionRegistry.registerBeanDefinition("messageSender", senderBuilder.getRawBeanDefinition());

    }
}
