package com.zhipuchina.event;

public interface AfterEventHandler extends EventHandler {
    public void process(int oldValue,int newValue);

    default public void process(){};
}
