package com.zhipuchina.function;

import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.model.OutputRegister;
import com.zhipuchina.utils.Buffer;

public class SixFunctionController implements FunctionController{
    @Override
    public byte[] serve(byte[] header,byte[] ADU) {
        int address = (ADU[1] << 8) | (ADU[2]& 0xFF);
        int value = (ADU[3] << 8) | (ADU[4]& 0xFF);
        //2+2+2+1+1+4
        byte[] out = new byte[12];
        Buffer.setValue(MemoryTypes.OutputRegister,address,value);
        System.arraycopy(ADU,0,out,7,ADU.length);
        int now = Integer.parseInt(Buffer.getValue(MemoryTypes.OutputRegister,address).toString());
        System.err.println("write "+ address +" value = " + now);
        return out;
    }
}
