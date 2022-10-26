package com.zhipuchina.function;

import com.zhipuchina.handler.GlobalLogger;
import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.model.OutputRegister;
import com.zhipuchina.utils.Buffer;
import com.zhipuchina.utils.ConvertTo;

/**
 * 写单个输出寄存器
 *
 * 请求ADU
 * 寄存器地址  2个字节
 * 寄存器值   2个字节
 *
 *  响应ADU
 * 输出地址   2个字节
 * 输出值    2个字节
 *
 * 异常响应(86)：
 * 异常码 1个字节
 */
public class SixFunctionController implements FunctionController{
    @Override
    public byte[] serve(byte[] header,byte[] ADU) {
        int address = ConvertTo.getShort(ADU[1],ADU[2]);
        byte[] value = new byte[]{ ADU[3] ,ADU[4] };
        //2+2+2+1+1+4
        byte[] out = new byte[12];
        Buffer.setValue(MemoryTypes.OutputRegister,address,value);
        System.arraycopy(ADU,0,out,7,ADU.length);
        GlobalLogger.logger.debug("write "+ address +" value = " + ConvertTo.getShort(Buffer.getValue(MemoryTypes.OutputRegister,address)));
        return out;
    }
}
