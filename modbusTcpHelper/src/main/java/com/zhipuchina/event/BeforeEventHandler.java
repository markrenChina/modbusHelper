package com.zhipuchina.event;

/**
 * 不能执行长时间耗时操作
 */
public interface BeforeEventHandler extends EventHandler{

    public boolean isNeedAsync();
    public void process(Integer oldValue,Integer newValue);

    default public void process(){};
}
