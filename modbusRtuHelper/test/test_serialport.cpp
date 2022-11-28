//
// Created by Ushop on 2022/11/18.
//

#include "modbus.h"
#include "serialport.h"
#include <cstring>

#ifdef WIN32
#define PLATFORM 1   //windows
#else
#define PLATFORM 2   //other
#endif

#ifdef WIN32
int main(int argv,char* argc[]){
    std::cout <<  (PLATFORM == 1 ? "Windows" : "Linux") << std::endl;
//    c9::SerialPort serialPort("\\\\.\\COM11");
    c9::SerialPort serialPort("COM6");
    serialPort.open();
    serialPort.write("hello world", sizeof("hello world"));
//    serialPort.read([](const std::string& data){
//        std::cout << data << std::endl;
//    });
    std::string buff;
    buff.resize(20);
    serialPort.read(&buff[0],20);
    std::cout << buff << std::endl;
    std::cout << PLATFORM ;
    Sleep(1000);
}
#else
int main(int argv,char* argc[]){
    std::cout <<  (PLATFORM == 1 ? "Windows" : "Linux") << std::endl;
    c9::SerialPort serialPort("/dev/ttyUART_485_2");
    std::string readBuff;
    readBuff.resize(100);
    char * cmd = (char *)"hello world!";
//    char cmd1[] =  {0x01,0x0F,0x00,0x00,0x00,0x01,0x01,0x00,0x2E,(char)0x97};
//    char cmd0[] =  {0x01,0x0F,0x00,0x00,0x00,0x01,0x01,0x01,(char)0xEF,0x57};
    int count = 0;
    while (count < INT8_MAX){
        //
        sleep(1);
        serialPort.write(cmd, sizeof(cmd));
        serialPort.read(&readBuff[0],100);
        std::cout << count << ":" << readBuff << std::endl;
        ::memset(&readBuff[0],0,100);
        sleep(2);
        count++;
    }
    serialPort.close();
}
#endif