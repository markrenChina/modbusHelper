package com.zhipuchina.event;

/**
 * 不能执行长时间耗时操作
 */
public interface BeforeEventHandler extends EventHandler{
    public void process(Object oldValue,Object newValue);

    default public void process(){};
}
