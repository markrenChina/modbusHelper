package com.zhipuchina.function;

import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.handler.ModbusTcpBasicSession;

//todo 异常
//@FunctionalInterface
public interface FunctionController {

    byte[] serve(byte[] header, byte[] ADU, ModbusTcpBasicSession session) throws ModbusException;
}
