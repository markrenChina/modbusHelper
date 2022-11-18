//
// Created by Ushop on 2022/11/18.
//
#include "modbus.h"
#include "serialport.h"

namespace c9{
    SerialPort::Callback::Callback(const std::function<void(char *)> &mCb) : m_cb(mCb) {}

    uint64_t
    SerialPort::open(std::string path, int baudrate, int stopBits, int dataBits, int parity, int flowCon) {
        return 0;
    }
}

