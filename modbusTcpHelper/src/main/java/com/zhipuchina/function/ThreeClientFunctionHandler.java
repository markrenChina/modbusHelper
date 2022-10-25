package com.zhipuchina.function;

public class ThreeClientFunctionHandler implements FunctionController{

    @Override
    public byte[] serve(byte[] header, byte[] ADU) {
        System.out.println("客户端收到功能码 3 ");
        return null;
    }
}
