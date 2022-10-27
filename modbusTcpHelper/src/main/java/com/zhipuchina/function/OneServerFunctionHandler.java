package com.zhipuchina.function;

import com.zhipuchina.handler.ModbusTcpBasicSession;
import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.utils.Buffer;

public class OneServerFunctionHandler implements FunctionController {

    /**
     * 响应PUD
     * 异常PDU 81 01
     * @param ADU
     * @return 一个数组，包含功能码及之后的数据
     */
    @Override
    public byte[] serve(byte[] header,byte[] ADU, ModbusTcpBasicSession session) {
        int address = (ADU[1] << 8) | (ADU[2] & 0xFF);
        int count = (ADU[3] << 8) | (ADU[4]& 0xFF);
        int outCount = count/8 + 1;
        //2+2+2+1+1+1+outCount
        byte[] out = new byte[9+outCount];
        //写功能码
        out[7] = ADU[0];
        out[8] = (byte) outCount;
        for (int i = 0 , pos =8 ; i < count; i++) {
            if (i%8 == 0){
                pos++;
            }
            boolean now = Boolean.parseBoolean(Buffer.getValue(MemoryTypes.OutputCoil,address + i).toString());
//            GlobalLogger.logger.debug("===========> now "  + now);
            int tmp =  0x01;
            for (int j = 0; j < i%8; j++) {
                tmp = tmp << 1;
            }
            if (now){
                out[pos] = (byte)( out[pos]| tmp );
//                out[pos] = (byte)(((out[pos] << 1)  | 0x01) & 0xFF);
            }/*else {
                out[pos] = (byte)(((out[pos] & 0xFF) << 1) & 0xFF);
            }*/
            //GlobalLogger.logger.debug("===========> out[pos]=" + );
        }
        return out;
    }
}
