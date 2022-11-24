package com.zhipuchina.function;

import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.utils.BitUtil;

public class OneServerFunctionReadHandler extends ServerFunctionReadTemplate {

    public OneServerFunctionReadHandler() {
        super(MemoryTypes.OutputCoil);
    }

    @Override
    public int getOutCount(int count) {
        return count/8 + 1;
    }

    @Override
    public void process(int[] value, byte[] out) {
//        for (int i = 0; i < value.length; i++) {
//            value[i] = BitUtil.reversal(value[i]);
//        }
//        System.arraycopy(value,0,out,9,value.length);
        for (int i = 0; i < value.length; i++) {
            out[9 + i/8] = BitUtil.setBit(out[9 + i/8],8 - i%8,value[i]);
        }
    }

    /**
     * 响应PUD
     * 异常PDU 81 01
     * @param ADU
     * @return 一个数组，包含功能码及之后的数据
     */


//    @Override
//    public byte[] serve(byte[] header,byte[] ADU, ModbusTcpBasicSession session) {
//        int address = ConvertTo.getInteger(ADU[1],ADU[2] );
//        int count = ConvertTo.getInteger(ADU[3],ADU[4] );
//        int outCount = count/8 + 1;
//        //2+2+2+1+1+1+outCount
//        byte[] out = new byte[9+outCount];
//        //写功能码
//        out[7] = ADU[0];
//        out[8] = (byte) outCount;
//        byte[] value = Buffer.getValue(MemoryTypes.OutputCoil, address, count);
//        System.out.println(Arrays.toString(value));
//        for (int i = 0; i < value.length; i++) {
//            value[i] = BitUtil.reversal(value[i]);
//        }
//        System.out.println(Arrays.toString(value));
//        System.arraycopy(value,0,out,9,value.length);
//        return out;
//    }
}
