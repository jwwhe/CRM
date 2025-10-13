package com.hxw.wscrm.common.annotaion;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *  该注解标识的方法执行的时候会被AOP来拦截。然后记录相关操作
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {
    String value() default " ";
}
