package com.zhipuchina.function;

import com.zhipuchina.handler.ModbusTcpBasicSession;

//todo 异常
@FunctionalInterface
public interface FunctionController {

    public byte[] serve(byte[] header, byte[] ADU, ModbusTcpBasicSession session);
}
