import com.ccand99.serialport.SerialPort;
import com.ccand99.serialport.SerialPortUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        SerialPort test = new SerialPort("COM3",9600,8,1);
        new Thread( () -> {
            for (int i = 0; i < 3; i++) {
                try {
                    test.write("hello world!".getBytes(StandardCharsets.UTF_8));
                    Thread.sleep(2000);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            test.close();
            System.exit(0);
        }).start();
//        byte[] buffer = test.read();
//        System.out.println(new String(buffer,StandardCharsets.UTF_8));

//        System.out.println(new String(test.readNBytes(5)));

        test.readCallback(bytes ->{
            System.out.println(new String(bytes));
        });

        Thread.sleep(5000);
        test.close();
//        InputStream in;
//        in.readNBytes()
    }
}