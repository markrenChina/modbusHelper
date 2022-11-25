#pragma once

#include "modbus.h"
#include "serialport.h"


//打开串口
//extern "C" JNIEXPORT jlong JNICALL
//Java_com_ccand99_SerialPortImpl_nativeOpen(JNIEnv *env, jobject thiz, jstring path, jint baudrate,
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
void java_nativeClose(JNIEnv *env, jobject thiz, jint fd);

//jobject java_getFileDescriptor(JNIEnv *env, jobject thiz,jlong flag,jobject _log);

jobject java_nativeOpen(JNIEnv *env, jobject thiz, jstring path, jint baudrate,
                        jint stopBits, jint dataBits, jint parity, jint flowCon,
                        jint flags);

jint JNI_OnLoad(JavaVM *vm,void *reserved) {
    JNIEnv *env;
    //获取JNI环境对象
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4)) {
        return JNI_ERR;
    };
    Jvm = vm;
    assert(env != nullptr);
    jclass j_class = env->FindClass("com/ccand99/serialport/SerialPortImpl");
    //jmethodID m = env->GetStaticMethodID(j_class,"postData", "(Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;DLjava/lang/String;)Ljava/lang/String;"
    JNINativeMethod methods[] = {
                {const_cast<char *>("nativeOpen"),
                 const_cast<char *>("(Ljava/lang/String;IIIIII)Ljava/io/FileDescriptor"), (void *) java_nativeOpen},
                {const_cast<char *>("nativeClose"),
                        const_cast<char *>("(I)V"), (void *) java_nativeClose}

    };
    env->RegisterNatives(j_class, methods, sizeof(methods) / sizeof(JNINativeMethod));
    env->DeleteLocalRef(j_class);
    return JNI_VERSION_1_4;
}
