package com.koala.utils.config.annotation;

import com.koala.utils.config.MyBatisConfigurationSingle;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Rocky on 16/2/23.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({MyBatisConfigurationSingle.class})
public @interface EnableDataSourceConfiguration {

    String[] profiles() default {"default"};
}
