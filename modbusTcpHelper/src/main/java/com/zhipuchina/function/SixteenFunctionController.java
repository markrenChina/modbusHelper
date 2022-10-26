package com.zhipuchina.function;

import com.zhipuchina.handler.GlobalLogger;
import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.model.OutputRegister;
import com.zhipuchina.utils.Buffer;

import java.util.Arrays;

/**
 * 写多个寄存器
 * 请求PDU
 * 起始地址   2个字节
 * 寄存器数量  2个字节
 *  字节数   1个字节  N=寄存器数量*2    ADU[5]
 * 寄存器值   N个字节
 */
public class SixteenFunctionController implements FunctionController{
    @Override
    public byte[] serve(byte[] header,byte[] ADU) {
        //地址
        int address = (ADU[1] << 8) | (ADU[2]& 0xFF);
        //寄存器数量
        int count = (ADU[3] << 8) | (ADU[4]& 0xFF);
        //字节数 6号位，数组角标5
        //2+2+2+1+1+2+2=12
        byte[] out = new byte[12];
        //写入到寄存器
        byte[] value = new byte[count*2];
        System.arraycopy(ADU,6,value,0,value.length);
//            int value = ((ADU[6 + i*2] & 0xFF) << 8) | (ADU[7 + i*2] &0xFF);
        Buffer.setValue(MemoryTypes.OutputRegister,address ,count,value);
        System.arraycopy(ADU,0,out,7,5);
        return out;
    }
}
