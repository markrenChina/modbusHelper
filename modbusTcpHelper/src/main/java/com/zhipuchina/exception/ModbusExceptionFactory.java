package com.zhipuchina.exception;

public class ModbusExceptionFactory {

    public static ModbusException create(int code){
        switch (code){
            case 1: return new IllegalFunctionException();
            case 2: return new IllegalDataAddressException();
            case 3: return new IllegalDataValueException();
            case 4: return new DeviceFailureException();
            case 5: return new AcknowledgeException();
            case 6: return new DeviceBusyException();
            case 7: return new NegativeAcknowledgeException();
            case 8: return new MemoryParityErrorException();
            case 0x0A: return new GatewayPathProblemException();
            case 0x0B: return new GatewayRefuseProblemException();
            case 0xFF: return new ExtendedExceptionResponseException();
            default: throw  new RuntimeException("exception code error");
        }
    }
}
