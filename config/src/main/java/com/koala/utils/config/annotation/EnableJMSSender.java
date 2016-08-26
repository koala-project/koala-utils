package com.koala.utils.config.annotation;

import com.koala.utils.config.JMSConfiguration;
import com.koala.utils.config.MessageSenderRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Rocky on 16/3/2.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({JMSConfiguration.class,MessageSenderRegistrar.class})
public @interface EnableJMSSender {

    /**
     * 需要发送消息的 Channel
     * @return
     */
    String queue();
}
