package com.koala.utils.gateway.annotation;

import com.koala.utils.gateway.define.MockApiImplementation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMockInterfaceImpl {
    Class<? extends MockApiImplementation> value();
}
