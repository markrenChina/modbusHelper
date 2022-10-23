package com.zhipuchina.function;

import com.zhipuchina.handler.GlobalLogger;
import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.model.OutputRegister;
import com.zhipuchina.utils.Buffer;

import java.util.Arrays;

/**
 * 写多个寄存器
 */
public class SixteenFunctionController implements FunctionController{
    @Override
    public byte[] serve(byte[] ADU) {
        //地址
        int address = (ADU[1] << 8) | (ADU[2]& 0xFF);
        //寄存器数量
        int count = (ADU[3] << 8) | (ADU[4]& 0xFF);
        //字节数 6号位，数组角标5
        //2+2+2+1+1+2+2=12
        byte[] out = new byte[12];
        //写入到寄存器
        for (int i = 0; i < count; i++) {
            int value = ((ADU[6 + i*2] & 0xFF) << 8) | (ADU[7 + i*2] &0xFF);
            GlobalLogger.logger.debug(value);
            Buffer.setValue(MemoryTypes.OutputRegister,address + i,value);
        }
        System.arraycopy(ADU,0,out,7,5);
        return out;
    }
}
