package com.zhipuchina.function;

public class FourFunctionHandler implements FunctionController{

    FunctionController functionController = new ThreeFunctionHandler();
    @Override
    public byte[] serve(byte[] header,byte[] ADU) {
        return functionController.serve(header,ADU);
    }
}
