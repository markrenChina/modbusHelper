//
// Created by markrenChina on 2022/11/18.
//
#pragma once
#include "modbus.h"


namespace c9{

//打开串口
    C9_EXPORTS int openSerial(const char *path, int baudrate, int stopBits, int dataBits, int parity, int flowCon, int flags);

    C9_EXPORTS void closeSerial(int fd);

    uint64_t getBaudrate(int baudrate);

    C9_EXPORTS ssize_t readSerial(int fd, char * buffer ,uint32_t size);
    C9_EXPORTS void writeSerial(int fd, char* data,int size);

    class SerialPort {
    public:
        using ptr = std::shared_ptr<SerialPort>;

        C9_EXPORTS SerialPort(const std::string &path);

        C9_EXPORTS SerialPort(const std::string &path, int baudrate, int stopBits, int dataBits, int parity, int flowCon,
                   int flags);

    public:
        C9_EXPORTS uint64_t open();
        C9_EXPORTS void close() const;
        /**
         * 同步读取
         * @param buffer 缓存区
         * @param size  缓冲区大小
         * @return 实际读取的大小
         */
        C9_EXPORTS ssize_t read(char * buffer ,uint32_t size) const;
        C9_EXPORTS void read(const std::function<void(std::string)>& mCb) const;

        C9_EXPORTS void write(char* data,int size) const;
    private:
        int fd;
        std::string path;
        int baudrate = 9600;
        int stopBits = 1;
        int dataBits = 8;
        int parity = 0;
        int flowCon = 0;
        int flags = 0;
    };
}


