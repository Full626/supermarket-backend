package com.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoleRequired {//自定义注解
    String[] value();  // 允许的角色：admin, manager, cashier
}