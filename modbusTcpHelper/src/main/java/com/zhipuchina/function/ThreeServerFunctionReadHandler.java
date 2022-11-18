package com.zhipuchina.function;

import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.utils.BitUtil;

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
public class ThreeServerFunctionReadHandler extends ServerFunctionReadTemplate {
    public ThreeServerFunctionReadHandler() {
        super(MemoryTypes.HoldingRegister);
    }

    @Override
    public int getOutCount(int count) {
        return count << 1;
    }

    @Override
    public void process(Integer[] value,byte[] out) {
        for (int i = 0; i < value.length; i++) {
            out[9+i*2] = BitUtil.getInt8To16(value[i]);
            out[10+i*2] = BitUtil.getInt0To8(value[i]);
        }
        //System.arraycopy(value,0,out,9,value.length);
    }


//    @Override
//    public byte[] serve(byte[] header,byte[] ADU, ModbusTcpBasicSession session) {
//        int address = ConvertTo.getInteger(ADU[1],ADU[2] );
//        int count = ConvertTo.getInteger(ADU[3],ADU[4] );
//        int outCount = count << 1;
//        //2+2+2+1+1+1+outCount
//        byte[] out = new byte[9 + outCount];
//        out[7] = ADU[0];
//        out[8] = (byte) outCount;
////        for (int i = 0; i < count; i++) {
////            int now = Integer.parseInt(Buffer.getValue(MemoryTypes.OutputRegister,address + i).toString());
////            out[9 + i*2] =(byte) ((now >> 8 )& 0xFF);
////            out[10 + i*2] = (byte) (now & 0xFF);
////        }
//        byte[] value = Buffer.getValue(MemoryTypes.HoldingRegister,address,count);
//        System.arraycopy(value,0,out,9,value.length);
//        return out;
//    }
}
