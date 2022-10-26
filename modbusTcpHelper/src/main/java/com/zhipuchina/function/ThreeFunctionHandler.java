package com.zhipuchina.function;

import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.utils.Buffer;

/**
 * 读保持寄存器
 *
 * 请求PDU
 *  起始地址 2个字节
 *  寄存器数量 2个字节
 *
 *  响应PDU
 *  字节数 1个字节
 *  寄存器值 N个字节
 *
 *  异常响应83
 *  异常码 1个字节
 */
public class ThreeFunctionHandler implements FunctionController{

    @Override
    public byte[] serve(byte[] header,byte[] ADU) {
        int address = (ADU[1] << 8) | (ADU[2]& 0xFF);
        int count = (ADU[3] << 8) | (ADU[4]& 0xFF);
        int outCount = count << 1;
        //2+2+2+1+1+1+outCount
        byte[] out = new byte[9 + outCount];
        out[7] = ADU[0];
        out[8] = (byte) outCount;
//        for (int i = 0; i < count; i++) {
//            int now = Integer.parseInt(Buffer.getValue(MemoryTypes.OutputRegister,address + i).toString());
//            out[9 + i*2] =(byte) ((now >> 8 )& 0xFF);
//            out[10 + i*2] = (byte) (now & 0xFF);
//        }
        byte[] value = Buffer.getValue(MemoryTypes.OutputRegister,address,count);
        System.arraycopy(value,0,out,9,value.length);
        return out;
    }
}
