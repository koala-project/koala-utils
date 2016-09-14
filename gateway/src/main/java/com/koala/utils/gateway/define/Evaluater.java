package com.koala.utils.gateway.define;

public interface Evaluater<TLeft, TRight> {
    void evaluate(TLeft left, TRight right);
}

