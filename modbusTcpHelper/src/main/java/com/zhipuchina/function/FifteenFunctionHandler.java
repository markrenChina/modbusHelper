package com.zhipuchina.function;

import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.model.OutputCoil;
import com.zhipuchina.utils.Buffer;

/**
 * 写多个线圈
 *
 * 请求PDU
 * 起始地址 2个字节
 * 输出数量 2个字节
 * 字节数   1个字节  N=线圈数量/8，余数不为0则加1
 * 输出值   N个字节
 *
 * 响应PDU
 * 输出地址  2个字节
 * 输出值    2个字节
 */
public class FifteenFunctionHandler implements FunctionController{
    @Override
    public byte[] serve(byte[] header,byte[] ADU) {
        int address = (ADU[1] << 8) | (ADU[2] & 0xFF);
        int count = (ADU[3] << 8) | (ADU[4]& 0xFF);
        byte[] out = new byte[12];
        for (int i = 0; i < count; i++) {
            int tmp = ADU[6];
            for (int j = 0; j < 8 - i; j++) {
                tmp |= tmp >> 1;
            }
            boolean value = (tmp & 0x01) == 1;
            Buffer.setValue(MemoryTypes.OutputCoil,address+i,value);
        }
        System.arraycopy(ADU,0,out,7,5);
        return out;
    }
}
