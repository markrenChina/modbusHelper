package com.zhipuchina;

import com.zhipuchina.client.ModbusTcpClient;
import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.handler.ModbusSyncTimer;
import com.zhipuchina.model.Buffer;
import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.utils.ConvertTo;
import junit.framework.TestCase;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{



    public void testClient() throws InterruptedException, ModbusException {
        Buffer.malloc(MemoryTypes.HoldingRegister, 0, 10);
        Buffer.malloc(MemoryTypes.OutputCoil, 0, 20);
        ModbusSyncTimer task = new ModbusSyncTimer(3, 1000);
//        ModbusTcpClient client =
//                new ModbusTcpClient(new InetSocketAddress("127.0.0.1", 8888), 1, task, null
//                );


        ModbusTcpClient client = new ModbusTcpClient(new InetSocketAddress("127.0.0.1", 8888), 1, null, null);
//        testRead(client);

//        Object o = client.readVSync(MemoryTypes.OutputCoil, 0,10);
//        System.out.println(o);
//
//        byte[] value = Buffer.getValue(MemoryTypes.OutputCoil, 0,10);
//        System.out.println(Arrays.toString(value));
        //client.write(MemoryTypes.OutputCoil, 8, true);
        client.writeV(MemoryTypes.OutputCoil,0, List.of(true,true,false,true,true,false,false,true,true));

        //        for (int i = 0; i < 100; i++) {
//            TimeUnit.SECONDS.sleep(1);
//            client.writeV(MemoryTypes.OutputRegister,1,null);
//        }
//        while (true){
//            TimeUnit.SECONDS.sleep(1);
//            byte[] value = Buffer.getValue(MemoryTypes.HoldingRegister, 0, 10);
//            System.out.println(Arrays.toString(value));
//        }

        //boolean b = client.writeV(MemoryTypes.HoldingRegister, 1, Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        //System.err.println(b);
//        client.read()

    }

    private static void testRead(ModbusTcpClient client) {
        Object read = client.readSync(MemoryTypes.HoldingRegister, 9);
        System.out.println(Arrays.toString(ConvertTo.primitive(read)));
        System.out.println("??????????????????");
        client.readAsync(MemoryTypes.HoldingRegister, 9, (value) -> {
            System.out.println("????????????????????????");
            System.out.println(Arrays.toString(ConvertTo.primitive(value.get(0))));
        });
        System.out.println("==========================");
    }

    public void testD(){

        System.out.println();
    }
//    public void testApp() throws UnknownHostException {
//        ModbusTcpServer modbusTcpServer = new ModbusTcpServer(InetAddress.getByName("127.0.0.1"), 8888, new MySessionFactory());
//        modbusTcpServer.start();
//    }


//    public void testBuffer(){
//        Buffer.malloc(MemoryTypes.InputCoil,100);
//        Boolean value = (Boolean) Buffer.getValue(MemoryTypes.InputCoil, 20);
//        GlobalLogger.logger.debug("???20????????????????????? " + value);
//        Buffer.malloc(MemoryTypes.OutputCoil,20);
//
//        Boolean value2 = (Boolean) Buffer.getValue(MemoryTypes.OutputCoil, 10);
//        GlobalLogger.logger.debug("???10????????????????????? " + value2);
//        Buffer.setValue(MemoryTypes.OutputCoil,10,true);
//        Boolean value3 = (Boolean) Buffer.getValue(MemoryTypes.OutputCoil, 10);
//        GlobalLogger.logger.debug("???10????????????????????? " + value3);
//
//        assertTrue( value3 );
//
//        Buffer.malloc(MemoryTypes.InputRegister,20);
//        int s1 = (int)Buffer.getValue(MemoryTypes.InputRegister,10);
//        GlobalLogger.logger.debug("???10????????????????????? " + s1);
//
//        Buffer.malloc(MemoryTypes.OutputRegister,50);
//        String s = Buffer.getString(OutputRegister.class,2,10);
//        GlobalLogger.logger.debug("?????????????????? ???2????????????10??????????????????" + s);
//        Buffer.setString(2,"hello");
//        String s2 = Buffer.getString(OutputRegister.class,2,10);
//        GlobalLogger.logger.debug("?????????????????? ???2????????????10??????????????????" + s2);
//    }


}
