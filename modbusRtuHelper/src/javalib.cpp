#include "modbus.h"
#include "javalib.h"
#include "serialport.h"

#include <iostream>

int java_nativeOpen
        (JNIEnv *env, jobject thiz, jstring path, jint baudrate, jint stopBits, jint dataBits,
         jint parity, jint flowCon, jint flags) {
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
    int len = env ->GetArrayLength(jb);
    std::string data;
    data.resize(len);
    env->GetByteArrayRegion(jb,0,len,(jbyte *)(&data[0]));
    c9::writeSerial((int)fd,&data[0],len);
    env->DeleteLocalRef(jb);
}

jbyteArray java_nativeRead(JNIEnv *env, jobject thiz,jint fd){
    std::string cByte;
    cByte.resize(3036);
    jsize bk = (jsize)c9::readSerial((int)fd,&(cByte[0]),3036);
    //std::cout <<"bk = " << bk << std::endl;
    jbyteArray jb = env->NewByteArray(bk);
    env->SetByteArrayRegion(jb,0,bk,(jbyte *)(&cByte[0]));
    //env->DeleteLocalRef(jb);
    return jb;
}

jbyteArray java_nativeReadNBytes(JNIEnv *env, jobject thiz,jint fd,jsize len){
    jsize readed = 0;
    std::string cByte;
    cByte.resize(len);
    int tmp = 0;
    while ( readed != len ){
        tmp = (int)c9::readSerial(fd,&(cByte[readed]),len-readed);
        if (tmp == EOF){
            jbyteArray jb = env->NewByteArray(readed);
            env->SetByteArrayRegion(jb,0,readed,(jbyte *)(&cByte[0]));
            return jb;
        }else {
            readed += tmp;
        }
    }
    jbyteArray jb = env->NewByteArray(readed);
    env->SetByteArrayRegion(jb,0,readed,(jbyte *)(&cByte[0]));
    return jb;
}

void java_nativeReadCallBack(JNIEnv *env, jobject thiz,jint fd,jobject callback){
    jbyteArray jb = java_nativeRead(env,thiz,fd);
    jclass jc = env->GetObjectClass(callback);
    jmethodID jmId = env->GetMethodID(jc,"recv","([B)V");
    env->CallVoidMethod(thiz,jmId,jb);
    env->DeleteLocalRef(jc);
}

//std::string jArray2cArray(JNIEnv *env,jbyteArray &_bytes){
//    jbyte *bytes;
//    bytes = env->GetByteArrayElements(_bytes, nullptr);
//    int chars_len = env->GetArrayLength(_bytes);
//    std::string chars;
//    chars.resize(chars_len);
//    memcpy(&(chars[0]), bytes, chars_len);
//    env->ReleaseByteArrayElements(_bytes, bytes, JNI_COMMIT);
//    return chars;
//}


