package com.zhipuchina.function;

import com.zhipuchina.model.MemoryTypes;

/**
 * 写单个线圈
 * 请求ADU
 *
 * 输出地址  2个字节
 * 输出值    2个字节
 *
 * 响应ADU
 * 输出地址  2个字节
 * 输出值    2个字节
 *
 * 异常响应(85 01)
 */
public class FiveServerFunctionHandler extends ServerFunctionWriteTemplate{
    public FiveServerFunctionHandler() {
        super(MemoryTypes.OutputCoil);
    }

    @Override
    public byte[] getValue(byte[] ADU) {

        return new byte[]{ (byte) ((ADU[3]&0xFF) >> 7) };
    }

    @Override
    public int getCount(byte[] ADU) {
        return 1;
    }
//    @Override
//    public byte[] serve(byte[] header,byte[] ADU, ModbusTcpBasicSession session) {
//        int address = ConvertTo.getInteger(ADU[1],ADU[2] );
//        boolean value = (ADU[3] >> 7) != 0;
//        //2+2+2+1+1+4
//        Buffer.setValue(MemoryTypes.OutputCoil,address,value);
//        byte[] out = new byte[12];
//        System.arraycopy(ADU,0,out,7,ADU.length);
//        return out;
//    }
}
