package com.zhipuchina.annotation;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//todo 改成区分同步异步
public @interface WorkThread {
}
