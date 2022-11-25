//
// Created by markrenChina on 2022/11/18.
//
#pragma once
#include "modbus.h"


namespace c9{

//打开串口
    int openSerial(const char *path, int baudrate, int stopBits, int dataBits, int parity, int flowCon, int flags);

    void closeSerial(int fd);

    uint64_t getBaudrate(int baudrate);

    class SerialPort {
    public:
        using ptr = std::shared_ptr<SerialPort>;

        explicit SerialPort(const std::string &path);

        C9_EXPORTS SerialPort(const std::string &path, int baudrate, int stopBits, int dataBits, int parity, int flowCon,
                   int flags);

    public:
        uint64_t open();
        void close() const;
        ssize_t read(char * buffer ,uint32_t size);
        void read(std::function<void(std::string)> mCb);

        void write(char* data,int size) const;
    private:
        int fd;
        std::string path;
        int baudrate = 9600;
        int stopBits = 1;
        int dataBits = 8;
        int parity = 0;
        int flowCon = 0;
        int flags;
    };
}


