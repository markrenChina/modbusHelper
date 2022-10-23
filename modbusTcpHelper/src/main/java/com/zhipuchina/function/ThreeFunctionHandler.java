package com.zhipuchina.function;

import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.utils.Buffer;

public class ThreeFunctionHandler implements FunctionController{

    /**
     * 响应PUD
     * 异常PDU 83 01
     * @param ADU
     * @return 一个数组，包含功能码及之后的数据
     */
    @Override
    public byte[] serve(byte[] ADU) {
        int address = (ADU[1] << 8) | (ADU[2]& 0xFF);
        int count = (ADU[3] << 8) | (ADU[4]& 0xFF);
        int outCount = count << 1;
        //2+2+2+1+1+1+outCount
        byte[] out = new byte[9 + outCount];
        out[7] = ADU[0];
        out[8] = (byte) outCount;
        for (int i = 0; i < count; i++) {
            int now = Integer.parseInt(Buffer.getValue(MemoryTypes.OutputRegister,address + i).toString());
            out[9 + i*2] =(byte) ((now >> 8 )& 0xFF);
            out[10 + i*2] = (byte) (now & 0xFF);
        }
        return out;
    }
}
