package com.zhipuchina.function;

import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.handler.ModbusTcpBasicSession;
import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.model.Buffer;
import com.zhipuchina.utils.ConvertTo;

public abstract class ServerFunctionReadTemplate implements FunctionController {

    protected MemoryTypes type;

    protected ServerFunctionReadTemplate(MemoryTypes type) {
        this.type = type;
    }

    public abstract int getOutCount(int count);
    public abstract void process(byte[] value, byte[] out);
    @Override
    public byte[] serve(byte[] header, byte[] ADU, ModbusTcpBasicSession session) throws ModbusException {
        int address = ConvertTo.getInteger(ADU[1],ADU[2] );
        int count = ConvertTo.getInteger(ADU[3],ADU[4] );
        int outCount = getOutCount(count);
        byte[] out = new byte[9 + outCount];
        out[7] = ADU[0];
        out[8] = (byte) outCount;
        byte[] value = Buffer.getValue(type,address,count);
        process(value, out);
        return out;
    }
}
