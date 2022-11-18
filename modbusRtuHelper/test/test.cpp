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

int main(int argv,char* argc[]){
    //c9::SerialPort serialPort{};
//    serialPort.readAsynch([](char* data){
//        std::cout << "Hello world" << std::endl;
//    });
    std::cout << PLATFORM ;
}