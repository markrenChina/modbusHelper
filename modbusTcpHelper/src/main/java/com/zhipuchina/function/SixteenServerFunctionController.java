package com.zhipuchina.function;

import com.zhipuchina.handler.ModbusTcpBasicSession;
import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.utils.Buffer;
import com.zhipuchina.utils.ConvertTo;

/**
 * 写多个寄存器
 * 请求PDU
 * 起始地址   2个字节
 * 寄存器数量  2个字节
 *  字节数   1个字节  N=寄存器数量*2    ADU[5]
 * 寄存器值   N个字节
 */
public class SixteenServerFunctionController implements FunctionController{

    @Override
    public byte[] serve(byte[] header,byte[] ADU, ModbusTcpBasicSession session) {
        //地址
        int address = ConvertTo.getShort(ADU[1],ADU[2]);
        //寄存器数量
        int count = ConvertTo.getShort(ADU[3],ADU[4]);
        //字节数 6号位，数组角标5
        //写入到寄存器
        byte[] value = new byte[count*2];
        System.arraycopy(ADU,6,value,0,value.length);
//            int value = ((ADU[6 + i*2] & 0xFF) << 8) | (ADU[7 + i*2] &0xFF);
        Buffer.setValue(MemoryTypes.HoldingRegister,address ,count,value);

        //2+2+2+1+1+2+2=12
        byte[] out = new byte[12];
        System.arraycopy(ADU,0,out,7,5);
        return out;
    }
}