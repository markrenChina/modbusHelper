//
// Created by Ushop on 2022/11/18.
//

#include "modbus.h"
#include "serialport.h"

#ifdef WIN32
#define PLATFORM 1;   //windows
#else
#define PLATFORM 2;   //other
#endif

#ifdef WIN32
int main(int argv,char* argc[]){
    c9::SerialPort serialPort("/");
//    serialPort.read([](const std::string& data){
//        std::cout << data << std::endl;
//    });
    std::cout << PLATFORM ;
}
#else
int main(int argv,char* argc[]){
    c9::SerialPort serialPort("/dev/test");
//    serialPort.read([](const std::string& data){
//        std::cout << data << std::endl;
//    });
    std::cout << PLATFORM ;
}
#endif