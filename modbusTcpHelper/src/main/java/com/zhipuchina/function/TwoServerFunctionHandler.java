package com.zhipuchina.function;

import com.zhipuchina.handler.ModbusTcpBasicSession;

public class TwoServerFunctionHandler implements FunctionController{

    FunctionController functionController = new OneServerFunctionHandler();

    @Override
    public byte[] serve(byte[] header,byte[] ADU, ModbusTcpBasicSession session) {
        return functionController.serve(header,ADU,session);
    }
}
