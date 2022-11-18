package com.zhipuchina.event;

public interface AfterEventHandler extends EventHandler {
    public void process(Integer oldValue,Integer newValue);

    default public void process(){};
}
