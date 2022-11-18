//
// Created by markrenChina on 2022/11/18.
//
#pragma once
#include "modbus.h"

namespace c9{
    class SerialPort {
    public:
        using ptr = std::shared_ptr<SerialPort>;
    public:
        uint64_t open();
        void close();
        void readSynch(char * buffer ,uint32_t size);
        void readAsynch();


    private:
        uint64_t fd = -1;
        std::string path;
        int baudrate = 9600;
        int stopBits = 1;
        int dataBits = 8;
        int parity = 0;
        int flowCon = 0;
        std::function<void(char *)> mCb;
    };
}


