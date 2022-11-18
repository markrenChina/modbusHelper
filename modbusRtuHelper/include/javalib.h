#pragma once

#include "modbus.h"


//打开串口
extern "C" JNIEXPORT jlong JNICALL
Java_com_ccand99_SerialPortImpl_nativeOpen(JNIEnv *env, jobject thiz, jstring path, jint baudrate,
                                           jint stopBits, jint dataBits, jint parity, jint flowCon,
                                           jint flags,jobject _log);

//创建停止读取的标志位
extern "C" JNIEXPORT jlong JNICALL
Java_com_ccand99_SerialPortImpl_createFlag(JNIEnv *env, jobject thiz);

extern "C"
JNIEXPORT void JNICALL
Java_com_ccand99_SerialPortImpl_nativeWrite(JNIEnv *env, jobject thiz, jbyteArray bytes,jobject _log);


extern "C"
JNIEXPORT jobject JNICALL Java_com_ccand99_SerialPortImpl_getFileDescriptor(JNIEnv *env, jobject thiz,
                                                                            jlong flag,jobject _log);

extern "C"
JNIEXPORT void JNICALL Java_com_ccand99_SerialPortImpl_read(JNIEnv *env, jobject thiz,jobject callback,jobject _log);

extern "C" JNIEXPORT void JNICALL
Java_com_ccand99_SerialPortImpl_nativeClose(JNIEnv *env, jobject thiz, jlong flag);