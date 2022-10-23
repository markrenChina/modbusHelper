package com.zhipuchina.handler;

import com.zhipuchina.log.DefaultLoggerImp;
import com.zhipuchina.log.ModBusLogAdapter;

public class GlobalLogger {
    public static ModBusLogAdapter logger = new DefaultLoggerImp();
}
