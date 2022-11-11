package com.zhipuchina;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{


//    public void testApp() throws UnknownHostException {
//        ModbusTcpServer modbusTcpServer = new ModbusTcpServer(InetAddress.getByName("127.0.0.1"), 8888, new MySessionFactory());
//        modbusTcpServer.start();
//    }


//    public void testBuffer(){
//        Buffer.malloc(MemoryTypes.InputCoil,100);
//        Boolean value = (Boolean) Buffer.getValue(MemoryTypes.InputCoil, 20);
//        GlobalLogger.logger.debug("第20个输入线圈值为 " + value);
//        Buffer.malloc(MemoryTypes.OutputCoil,20);
//
//        Boolean value2 = (Boolean) Buffer.getValue(MemoryTypes.OutputCoil, 10);
//        GlobalLogger.logger.debug("第10个输出线圈值为 " + value2);
//        Buffer.setValue(MemoryTypes.OutputCoil,10,true);
//        Boolean value3 = (Boolean) Buffer.getValue(MemoryTypes.OutputCoil, 10);
//        GlobalLogger.logger.debug("第10个输出线圈值为 " + value3);
//
//        assertTrue( value3 );
//
//        Buffer.malloc(MemoryTypes.InputRegister,20);
//        int s1 = (int)Buffer.getValue(MemoryTypes.InputRegister,10);
//        GlobalLogger.logger.debug("第10个只读寄存器为 " + s1);
//
//        Buffer.malloc(MemoryTypes.OutputRegister,50);
//        String s = Buffer.getString(OutputRegister.class,2,10);
//        GlobalLogger.logger.debug("读输出寄存器 第2位开始，10个寄存器长度" + s);
//        Buffer.setString(2,"hello");
//        String s2 = Buffer.getString(OutputRegister.class,2,10);
//        GlobalLogger.logger.debug("读输出寄存器 第2位开始，10个寄存器长度" + s2);
//    }
}
