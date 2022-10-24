package com.zhipuchina.event;

import com.zhipuchina.model.Memory;

@FunctionalInterface
public interface EventHandler {
    public void process();
}
