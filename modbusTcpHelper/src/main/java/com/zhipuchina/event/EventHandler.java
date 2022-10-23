package com.zhipuchina.event;

import com.zhipuchina.model.Memory;

public interface EventHandler {

    default public void beforeProcess(Object oldValue,Object newValue){};

    default public void afterProcess(Object oldValue,Object newValue){};
}
