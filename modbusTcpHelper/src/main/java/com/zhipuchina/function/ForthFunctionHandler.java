package com.zhipuchina.function;

public class ForthFunctionHandler implements FunctionController{

    FunctionController functionController = new ThreeFunctionHandler();
    @Override
    public byte[] serve(byte[] ADU) {
        return functionController.serve(ADU);
    }
}
