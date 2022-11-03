package com.zhipuchina.function;

import com.zhipuchina.handler.ModbusTcpBasicSession;
import com.zhipuchina.utils.ConvertTo;

public abstract class ServerFunctionTemplate implements FunctionController {

    public abstract int getOutCount(int count);
    public abstract void recv(int count, byte[] out, int address);
    @Override
    public byte[] serve(byte[] header, byte[] ADU, ModbusTcpBasicSession session) {
        int address = ConvertTo.getInteger(ADU[1],ADU[2] );
        int count = ConvertTo.getInteger(ADU[3],ADU[4] );
        int outCount = getOutCount(count);
        byte[] out = new byte[9 + outCount];
        out[7] = ADU[0];
        out[8] = (byte) outCount;
        recv(count, out, address);
        return out;
    }
}
