package com.zhipuchina.function;

import com.zhipuchina.handler.ModbusTcpBasicSession;
import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.utils.BitUtil;
import com.zhipuchina.utils.Buffer;
import com.zhipuchina.utils.ConvertTo;

import java.util.Arrays;

public class OneServerFunctionHandler extends ServerFunctionTemplate {
    @Override
    public int getOutCount(int count) {
        return count/8 + 1;
    }

    @Override
    public void recv(int count, byte[] out, int address) {
        byte[] value = Buffer.getValue(MemoryTypes.OutputCoil, address, count);
        System.out.println(Arrays.toString(value));
        for (int i = 0; i < value.length; i++) {
            value[i] = BitUtil.reversal(value[i]);
        }
        System.out.println(Arrays.toString(value));
        System.arraycopy(value,0,out,9,value.length);
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
