package com.zhipuchina.log;

public interface ModBusLogAdapter {
    public void info(Object obj);
    public void debug(Object obj);
    public void warn(Object obj);
    public void error(Object obj);
}
