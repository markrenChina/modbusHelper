package com.zhipuchina.event;

public interface AfterEventHandler extends EventHandler {
    public void process(Object oldValue,Object newValue);

    default public void process(){};
}
