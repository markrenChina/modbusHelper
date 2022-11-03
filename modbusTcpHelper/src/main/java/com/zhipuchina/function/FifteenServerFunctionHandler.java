package com.zhipuchina.function;

import com.zhipuchina.handler.ModbusTcpBasicSession;
import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.utils.Buffer;
import com.zhipuchina.utils.ConvertTo;

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
public class FifteenServerFunctionHandler implements FunctionController{
    @Override
    public byte[] serve(byte[] header,byte[] ADU, ModbusTcpBasicSession session) {
        int address = ConvertTo.getInteger(ADU[1],ADU[2] );
        int count = ConvertTo.getInteger(ADU[3],ADU[4] );
        byte[] out = new byte[12];
//        for (int i = 0; i < count; i++) {
//            int tmp = ADU[6];
//            for (int j = 0; j < 8 - i; j++) {
//                tmp |= tmp >> 1;
//            }
//            boolean value = (tmp & 0x01) == 1;
//            Buffer.setValue(MemoryTypes.OutputCoil,address+i,value);
//        }
        byte[] value = new byte[count/8+1];
        System.arraycopy(ADU,6,value,0,value.length);
        Buffer.setValue(MemoryTypes.OutputCoil,address,value);
        System.arraycopy(ADU,0,out,7,5);
        return out;
    }
}
