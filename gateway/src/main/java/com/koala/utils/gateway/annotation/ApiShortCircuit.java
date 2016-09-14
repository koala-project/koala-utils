package com.koala.utils.gateway.annotation;

import com.koala.utils.gateway.define.MockApiReturnObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiShortCircuit {
    Class<? extends MockApiReturnObject> value();
}
