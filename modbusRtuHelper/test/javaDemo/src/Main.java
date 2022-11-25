import com.ccand99.serialport.SerialPort;
import com.ccand99.serialport.SerialPortUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws IOException {
        SerialPort test = new SerialPort("COM3",9600,8,1);
        test.write("hello world!".getBytes(StandardCharsets.UTF_8));
        byte[] buffer = new byte[100];
        test.read(buffer);
        System.out.println(new String(buffer,StandardCharsets.UTF_8));
    }
}