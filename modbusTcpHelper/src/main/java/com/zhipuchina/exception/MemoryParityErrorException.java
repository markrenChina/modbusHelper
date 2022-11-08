package com.zhipuchina.exception;

//内存奇偶校验错误
public class MemoryParityErrorException extends ModbusException {

    public MemoryParityErrorException() {
        super(8);
    }

    @Override
    public String toString() {
        return "The slave attempted to read extended\n" +
                "memory, but detected a parity error in memory.\n" +
                "The master can retry the request, but service\n" +
                "may be required at the slave device";
    }
}
