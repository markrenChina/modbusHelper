#include "modbus.h"
#include "javalib.h"
#include "serialport.h"

#include <iostream>

jobject java_nativeOpen
        (JNIEnv *env, jobject thiz, jstring path, jint baudrate, jint stopBits, jint dataBits,
         jint parity, jint flowCon, jint flags) {
    jobject mFileDescriptor;
    const char *path_utf = (*env).GetStringUTFChars(path, nullptr);
    int fd = c9::openSerial(path_utf, baudrate, stopBits, dataBits, parity, flowCon, flags);
    if (fd == -1) {
        return nullptr;
    }
    (*env).ReleaseStringUTFChars(path, path_utf);

    /* Create a corresponding file descriptor */
    {
        jclass cFileDescriptor = (*env).FindClass("java/io/FileDescriptor");
        jmethodID iFileDescriptor = (*env).GetMethodID(cFileDescriptor, "<init>", "()V");
        jfieldID descriptorID = (*env).GetFieldID(cFileDescriptor, "descriptor", "I");
        mFileDescriptor = (*env).NewObject(cFileDescriptor, iFileDescriptor);
        (*env).SetIntField(mFileDescriptor, descriptorID, (jint) fd);
    }

    return mFileDescriptor;
}

void java_nativeClose(JNIEnv *env, jobject thiz, jint fd) {
    if (fd > 0){
        c9::closeSerial((int)fd);
    }
}
