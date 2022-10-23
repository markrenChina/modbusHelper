package com.zhipuchina.function;

import com.zhipuchina.handler.GlobalLogger;
import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.model.OutputCoil;
import com.zhipuchina.model.OutputRegister;
import com.zhipuchina.utils.Buffer;

public class FiveFunctionController implements FunctionController{
    @Override
    public byte[] serve(byte[] ADU) {
        int address = (ADU[1] << 8) | (ADU[2]& 0xFF);
        GlobalLogger.logger.debug("address "+ address);
        boolean value = (ADU[3] >> 7) != 0;
        GlobalLogger.logger.debug("write value " + value);
        //2+2+2+1+1+4
        byte[] out = new byte[12];
        Buffer.setValue(MemoryTypes.OutputCoil,address,value);
        System.arraycopy(ADU,0,out,7,ADU.length);
        boolean now = Boolean.parseBoolean(Buffer.getValue(MemoryTypes.OutputCoil,address).toString());
        GlobalLogger.logger.debug("写完之后" + address +"的线圈为："+ now);
        return out;
    }
}
