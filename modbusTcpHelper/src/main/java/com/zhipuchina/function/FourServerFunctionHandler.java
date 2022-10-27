package com.zhipuchina.function;

import com.zhipuchina.handler.ModbusTcpBasicSession;

public class FourServerFunctionHandler implements FunctionController{

    FunctionController functionController = new ThreeServerFunctionHandler();
    @Override
    public byte[] serve(byte[] header,byte[] ADU, ModbusTcpBasicSession session) {
        return functionController.serve(header,ADU,session);
    }
}
