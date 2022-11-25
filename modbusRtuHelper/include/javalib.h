#pragma once

#include "modbus.h"
#include "serialport.h"


//打开串口
//extern "C" JNIEXPORT jlong JNICALL
//Java_com_ccand99_serialport_SerialPortImpl_nativeOpen(JNIEnv *env, jobject thiz, jstring path, jint baudrate,
//                                           jint stopBits, jint dataBits, jint parity, jint flowCon,
//                                           jint flags,jobject _log);
//
////创建停止读取的标志位
//extern "C" JNIEXPORT jlong JNICALL
//Java_com_ccand99_SerialPortImpl_createFlag(JNIEnv *env, jobject thiz);
//
//extern "C"
//JNIEXPORT void JNICALL
//Java_com_ccand99_SerialPortImpl_nativeWrite(JNIEnv *env, jobject thiz, jbyteArray bytes,jobject _log);

static JavaVM * Jvm;

int java_nativeOpen(JNIEnv *env, jobject thiz, jstring path, jint baudrate,
                    jint stopBits, jint dataBits, jint parity, jint flowCon,
                    jint flags);

void java_nativeClose(JNIEnv *env, jobject thiz, int fd);

void java_nativeWrite(JNIEnv *env, jobject thiz,jint fd,jbyteArray jb);
int java_nativeRead(JNIEnv *env, jobject thiz,jint fd, jbyteArray jb);

char* jbyteArray2charArray(JNIEnv *env,jbyteArray &_bytes);


jint JNI_OnLoad(JavaVM *vm,void *reserved) {
    JNIEnv *env;
    //获取JNI环境对象
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4)) {
        std::cerr << "JNI_OnLoad error" << std::endl;
        return JNI_ERR;
    };
    //std::cout << "JNI_OnLoad" << std::endl;
    Jvm = vm;
    assert(env != nullptr);
    jclass j_class = env->FindClass("com/ccand99/serialport/SerialPortUtils");
    JNINativeMethod methods[] = {
                {(char *)"nativeOpen",
                 (char *)"(Ljava/lang/String;IIIIII)I", (void *) java_nativeOpen},
                {(char *)("nativeClose"),
                        (char *)("(I)V"), (void *) java_nativeClose},
                {(char *)"nativeWrite",
                        (char *)"(I[B)V",(void *) java_nativeWrite},
                {(char *)"nativeRead",
                        (char *)"(I[B)I",(void *) java_nativeRead}

    };
    env->RegisterNatives(j_class, methods, sizeof(methods) / sizeof(JNINativeMethod));
    std::cout << "RegisterNatives finnsih" << std::endl;
    //env->DeleteLocalRef(j_class);
    return JNI_VERSION_1_4;
}
