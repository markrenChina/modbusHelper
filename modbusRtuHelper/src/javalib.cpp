#include "modbus.h"
#include "javalib.h"
#include "serialport.h"

#include <iostream>

int java_nativeOpen
        (JNIEnv *env, jobject thiz, jstring path, jint baudrate, jint stopBits, jint dataBits,
         jint parity, jint flowCon, jint flags) {
    jobject mFileDescriptor;
    const char *path_utf = (*env).GetStringUTFChars(path, nullptr);
    int fd = c9::openSerial(path_utf, baudrate, stopBits, dataBits, parity, flowCon, flags);
    (*env).ReleaseStringUTFChars(path, path_utf);
    return fd;
}

void java_nativeClose(JNIEnv *env, jobject thiz, int fd) {
    if (fd > 0){
        c9::closeSerial((int)fd);
    }
}

void java_nativeWrite(JNIEnv *env, jobject thiz,jint fd, jbyteArray jb){
    //c9::
    uint64_t dword = 0;
    int len = env ->GetArrayLength(jb);
    char* data = jbyteArray2charArray(env,jb);
    c9::writeSerial((int)fd,data,len);
}

int java_nativeRead(JNIEnv *env, jobject thiz,jint fd, jbyteArray jb){
    return 0;
}

char* jbyteArray2charArray(JNIEnv *env,jbyteArray &_bytes){
    //jbyte * bBuffer = env ->GetByteArrayElements(_bytes,nullptr);
    char *chars = nullptr;
    jbyte *bytes;
    bytes = env->GetByteArrayElements(_bytes, nullptr);
    int chars_len = env->GetArrayLength(_bytes);
    chars = new char[chars_len + 1];
    memset(chars,0,chars_len + 1);
    memcpy(chars, bytes, chars_len);
    chars[chars_len] = 0;
    env->ReleaseByteArrayElements(_bytes, bytes, 0);
    return chars;
}


