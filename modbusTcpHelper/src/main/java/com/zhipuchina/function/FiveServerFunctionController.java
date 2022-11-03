package com.zhipuchina.function;

import com.zhipuchina.handler.GlobalLogger;
import com.zhipuchina.handler.ModbusTcpBasicSession;
import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.utils.Buffer;
import com.zhipuchina.utils.ConvertTo;

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
public class FiveServerFunctionController implements FunctionController{
    @Override
    public byte[] serve(byte[] header,byte[] ADU, ModbusTcpBasicSession session) {
        int address = ConvertTo.getInteger(ADU[1],ADU[2] );
        GlobalLogger.logger.debug("address "+ address);
        boolean value = (ADU[3] >> 7) != 0;
        GlobalLogger.logger.debug("write value " + value);
        //2+2+2+1+1+4
        byte[] out = new byte[12];
        Buffer.setValue(MemoryTypes.OutputCoil,address,value);
        System.arraycopy(ADU,0,out,7,ADU.length);
        boolean now = Buffer.getValue(MemoryTypes.OutputCoil,address)[0] == 0x01;
        GlobalLogger.logger.debug("写完之后" + address +"的线圈为："+ now);
        return out;
    }
}
