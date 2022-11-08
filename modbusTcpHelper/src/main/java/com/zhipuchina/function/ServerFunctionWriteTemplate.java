package com.zhipuchina.function;

import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.handler.ModbusTcpBasicSession;
import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.utils.Buffer;
import com.zhipuchina.utils.ConvertTo;

public abstract class ServerFunctionWriteTemplate implements FunctionController{

    protected MemoryTypes type;

    public ServerFunctionWriteTemplate(MemoryTypes type) {
        this.type = type;
    }

    public abstract byte[] getValue(byte[] ADU);

    @Override
    public byte[] serve(byte[] header, byte[] ADU, ModbusTcpBasicSession session) throws ModbusException {
        //地址
        int address = ConvertTo.getShort(ADU[1],ADU[2]);
        byte[] value = getValue(ADU);
        //写入
        Buffer.setValue(type,address,value);
        byte[] out = new byte[12];
        System.arraycopy(ADU,0,out,7,5);
        return out;
    }
}
