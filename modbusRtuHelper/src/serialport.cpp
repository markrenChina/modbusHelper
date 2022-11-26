//
// Created by Ushop on 2022/11/18.
//
#include "modbus.h"
#include "serialport.h"

namespace c9 {

    uint64_t
    SerialPort::open() {
        return fd = openSerial(path.c_str(), baudrate, stopBits, dataBits, parity, flowCon, flags);
    }

    C9_EXPORTS SerialPort::SerialPort(const std::string &path) : path(path) {}

    C9_EXPORTS SerialPort::SerialPort(const std::string &path, int baudrate, int stopBits, int dataBits, int parity, int flowCon,
                           int flags) : path(path), baudrate(baudrate), stopBits(stopBits), dataBits(dataBits),
                                        parity(parity), flowCon(flowCon), flags(flags) {}

    C9_EXPORTS void SerialPort::close() const {
        if (fd != -1) {
            closeSerial(fd);
        }
    }


#ifdef WIN32

    C9_EXPORTS ssize_t readSerial(int fd,char *buffer, uint32_t size) {
        OVERLAPPED m_osRead;
        memset(&m_osRead, 0, sizeof m_osRead);
        m_osRead.hEvent = CreateEventA(nullptr, TRUE, FALSE, "READ Event");
        BOOL readStat = ReadFile((HANDLE) _get_osfhandle(fd), buffer, size, nullptr, &m_osRead);
        if (!readStat) {
            if (GetLastError() == ERROR_IO_PENDING) {
                WaitForSingleObject(m_osRead.hEvent, INFINITE);
            }
        }else {
            return EOF;
        }
        return m_osRead.InternalHigh;
    }

    C9_EXPORTS void writeSerial(int fd,char *data, int size) {
        DWORD dword = 0;
        OVERLAPPED m_osWrite;
        memset(&m_osWrite, 0, sizeof m_osWrite);
        m_osWrite.hEvent = CreateEventA(nullptr, TRUE, FALSE, "WRITE Event");
        BOOL writeState = WriteFile((HANDLE) _get_osfhandle(fd), data, size, &dword, &m_osWrite);
        if (!writeState) {
            if (GetLastError() == ERROR_IO_PENDING) {
                WaitForSingleObject(m_osWrite.hEvent, 1000);
            }
        }
    }

#else
    ssize_t readSerial(int fd,char *buffer, uint32_t size) {
        return ::read(fd,buffer,size);
    }

    void writeSerial(int fd,char *data,int size)  {
        ::write(fd,data,size);
    }

#endif

    C9_EXPORTS void SerialPort::read(std::function<void(std::string)> mCb) {
        char *buffer = new char[3036];
        while (read(buffer, 3036) != EOF) {
            mCb(buffer);
        }
    }

    ssize_t SerialPort::read(char *buffer, uint32_t size) const {
        return readSerial(fd,buffer,size);
    }

    void SerialPort::write(char *data, int size) const {
        writeSerial(fd,data,size);
    }

#ifdef WIN32

    C9_EXPORTS void closeSerial(int fd) {
        CloseHandle(reinterpret_cast<HANDLE>(_get_osfhandle(fd)));
    }

    int openSerial(const char *path, int baudrate, int stopBits, int dataBits, int parity, int flowCon, int flags) {
        HANDLE fd;
        DWORD baudRate;
        /* Check arguments */
        {
            baudRate = getBaudrate(baudrate);
            if (baudRate == -1) {
                std::cerr << "INVALID BAUDRATE" << std::endl;
                return -1;
            }
        }
        fd = CreateFileA(
                path,
                GENERIC_READ | GENERIC_WRITE | flags,
                0,//禁止其他程序读写
                nullptr,
                OPEN_EXISTING,//如果存在就打开
                FILE_FLAG_OVERLAPPED,//异步方式: FILE_FLAG_OVERLAPPED
                nullptr
        );
        {
            //Configure device
            //初始通信设备的参数
            //2. 设备内部输入缓冲区建议大小，字节
            //3. 设备内部输出缓冲区建议大小，字节
            if (!SetupComm(fd, 2048, 1024)) {
                std::cerr << "SetupComm error" << std::endl;
                return -3;
            }
            DCB dcb;
            if (!GetCommState(fd, &dcb)) {
                memset(&dcb, 0, sizeof(DCB));
                dcb.DCBlength = sizeof dcb;
            }
            //波特率
            dcb.BaudRate = CBR_9600;
            //数据位
            switch (dataBits) {
                case 5:
                case 6:
                case 7:
                case 8: {
                    dcb.ByteSize = (BYTE) dataBits;
                    break;
                }
                default: {
                    dcb.ByteSize = 8;
                    std::cerr << "dataBits error" << std::endl;
                    break;
                }
            }
            //奇偶校验
            switch (parity) {
                case ODDPARITY:
                case EVENPARITY:
                case MARKPARITY:
                case SPACEPARITY:
                    dcb.fParity = TRUE;
                    dcb.Parity = parity;
                    break;
                case NOPARITY:
                default:
                    dcb.fParity = FALSE;
                    dcb.Parity = NOPARITY;
                    break;
            }
            //停止位
            dcb.StopBits = (BYTE) stopBits;
            //todo 通过flowCon 设置下面的
            dcb.fDtrControl = DTR_CONTROL_DISABLE;//DTR控制
            dcb.fRtsControl = RTS_CONTROL_DISABLE;//RTS控制
            dcb.fOutxCtsFlow = FALSE;    //CTS线上的硬件握手
            dcb.fOutxDsrFlow = FALSE;    //DST线上的硬件握手

            dcb.fDsrSensitivity = FALSE;
            dcb.fTXContinueOnXoff = FALSE;//
            dcb.fOutX = FALSE;            //是否使用XON/XOFF协议
            dcb.fInX = FALSE;            //是否使用XON/XOFF协议
            dcb.fErrorChar = FALSE;        //是否使用发送错误协议
            dcb.fNull = FALSE;            //停用null stripping
            dcb.fAbortOnError = FALSE;    //串口发送错误，并不终止串口读写
            if (!SetCommState(fd, &dcb)) {
                DWORD error = GetLastError();
                std::cerr << "SetCommState error: "  << error << std::endl;
                return -4;
            }
            //超时参数
            COMMTIMEOUTS CommTimeOuts;
            GetCommTimeouts(fd, &CommTimeOuts);
            /**
            * 以ms为单位指定通信线路上两个字符到达之间的最大时间间隔。
            * 在ReadFile()操作期间，从接收到第一个字符时开始计时。如果任意两个字符到达之间的时间间隔超过这个最大值，
            * 则ReadFile()操作完成，并返回缓冲数据。如果被置为0，则表示不使用间隔超时。
            */
            CommTimeOuts.ReadIntervalTimeout = 1000;
            CommTimeOuts.ReadTotalTimeoutMultiplier = 0;
            CommTimeOuts.ReadTotalTimeoutConstant = 0;
            CommTimeOuts.WriteTotalTimeoutMultiplier = 10;
            CommTimeOuts.WriteTotalTimeoutConstant = 100;
            if (!SetCommTimeouts(fd, &CommTimeOuts)) {
                std::cerr << "SetCommTimeouts error" << std::endl;
                return -5;
            }
        }
        {
            //清除缓冲区
            PurgeComm(fd, PURGE_TXCLEAR | PURGE_RXCLEAR);
        }


        if (fd == INVALID_HANDLE_VALUE) {
            std::cerr << "INVALID FD" << std::endl;
            return -1;
        }
        return _open_osfhandle((intptr_t) fd, O_RDWR | flags);   //_get_osfhandle
    }

    uint64_t getBaudrate(int baudrate) {
        switch (baudrate) {
            case 110:
                return CBR_110;
            case 300:
                return CBR_300;
            case 600:
                return CBR_600;
            case 1200:
                return CBR_1200;
            case 2400:
                return CBR_2400;
            case 4800:
                return CBR_4800;
            case 9600:
                return CBR_9600;
            case 14400:
                return CBR_14400;
            case 19200:
                return CBR_19200;
            case 38400:
                return CBR_38400;
            case 56000:
                return CBR_56000;
            case 57600:
                return CBR_57600;
            case 115200:
                return CBR_115200;
            case 128000:
                return CBR_128000;
            case 256000:
                return CBR_256000;
            default:
                return -1;
        }
    }

#else
    void closeSerial(int fd) {
        close(fd);
    }

    int openSerial(const char *path, int baudrate, int stopBits, int dataBits, int parity, int flowCon, int flags) {
        int fd;
        speed_t speed;
/* Check arguments */
        {
            speed = (speed_t)getBaudrate(baudrate);
            if (speed == (speed_t)-1) {
                std::cerr << "INVALID BAUDRATE" << std::endl;
                return -1;
            }
        }

/* Opening device */
        {
            fd = open(path, O_RDWR | flags);
            if (fd == -1) {
                std::cerr << "INVALID FD" << std::endl;
                return -1;
            }
        }

/* Configure device */
        {
            struct termios cfg;
            if (tcgetattr(fd, &cfg)) {
                close(fd);
                return -1;
            }

            cfmakeraw(&cfg);
            cfsetispeed(&cfg, speed);
            cfsetospeed(&cfg, speed);

            cfg.c_cflag &= ~CSIZE;
            switch (dataBits) {
                case 5:
                    cfg.c_cflag |= CS5;    //Use 5-bit data bits
                    break;
                case 6:
                    cfg.c_cflag |= CS6;    //Use 6-bit data bits
                    break;
                case 7:
                    cfg.c_cflag |= CS7;    //Use 7-bit data bits
                    break;
                case 8:
                    cfg.c_cflag |= CS8;    //Use 8-bit data bits
                    break;
                default:
                    cfg.c_cflag |= CS8;
                    break;
            }

            switch (parity) {
                case 0:
                    cfg.c_cflag &= ~PARENB;    // None parity
                    break;
                case 1:
                    cfg.c_cflag |= (PARODD | PARENB);   // Odd parity
                    break;
                case 2:
                    cfg.c_iflag &= ~(IGNPAR | PARMRK); // Even parity
                    cfg.c_iflag |= INPCK;
                    cfg.c_cflag |= PARENB;
                    cfg.c_cflag &= ~PARODD;
                    break;
                default:
                    cfg.c_cflag &= ~PARENB;
                    break;
            }

            switch (stopBits) {
                case 1:
                    cfg.c_cflag &= ~CSTOPB;    // 1 bit stop bit
                    break;
                case 2:
                    cfg.c_cflag |= CSTOPB;    // 2 bit stop bit
                    break;
                default:
                    break;
            }

// hardware flow control
            switch (flowCon) {
                case 0:
                    cfg.c_cflag &= ~CRTSCTS;    // None flow control
                    break;
                case 1:
                    cfg.c_cflag |= CRTSCTS;    // Hardware flow control
                    break;
                case 2:
                    cfg.c_cflag |= IXON | IXOFF | IXANY;    // Software flow control
                    break;
                default:
                    cfg.c_cflag &= ~CRTSCTS;
                    break;
            }


            if (tcsetattr(fd, TCSANOW, &cfg)) {
//            LOGE("tcsetattr() failed");
                close(fd);
                return -1;
            }
        }
        return fd;
    }

    uint64_t getBaudrate(int baudrate) {
        switch (baudrate) {
            case 0:
                return B0;
            case 50:
                return B50;
            case 75:
                return B75;
            case 110:
                return B110;
            case 134:
                return B134;
            case 150:
                return B150;
            case 200:
                return B200;
            case 300:
                return B300;
            case 600:
                return B600;
            case 1200:
                return B1200;
            case 1800:
                return B1800;
            case 2400:
                return B2400;
            case 4800:
                return B4800;
            case 9600:
                return B9600;
            case 19200:
                return B19200;
            case 38400:
                return B38400;
            case 57600:
                return B57600;
            case 115200:
                return B115200;
            case 230400:
                return B230400;
            case 460800:
                return B460800;
            case 500000:
                return B500000;
            case 576000:
                return B576000;
            case 921600:
                return B921600;
            case 1000000:
                return B1000000;
            case 1152000:
                return B1152000;
            case 1500000:
                return B1500000;
            case 2000000:
                return B2000000;
            case 2500000:
                return B2500000;
            case 3000000:
                return B3000000;
            case 3500000:
                return B3500000;
            case 4000000:
                return B4000000;
            default:
                return -1;
        }
    }
#endif
}

