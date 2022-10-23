package com.zhipuchina.log;


import java.util.ResourceBundle;

public class DefaultLoggerImp implements ModBusLogAdapter{
    @Override
    public void info(Object obj) {
        System.out.println(obj);
    }

    @Override
    public void debug(Object obj) {
        System.out.println(obj);
    }

    @Override
    public void warn(Object obj) {
        System.out.println(obj);
    }

    @Override
    public void error(Object obj) {
        System.err.println(obj);
    }
}
