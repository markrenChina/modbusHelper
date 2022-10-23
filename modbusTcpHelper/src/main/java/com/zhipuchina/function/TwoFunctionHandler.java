package com.zhipuchina.function;

public class TwoFunctionHandler implements FunctionController{

    FunctionController functionController = new OneFunctionHandler();

    @Override
    public byte[] serve(byte[] ADU) {
        return functionController.serve(ADU);
    }
}
