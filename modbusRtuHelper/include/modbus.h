//
// Created by markrenChina on 2022/11/18.
//
#pragma once

#include <jni.h>
#include <iostream>
#include <sstream>
#include <memory>
#include <string>
#include <functional>
#include <cassert>
#include <fcntl.h>

#ifndef CV_EXPORTS
# if (defined _WIN32 || defined WINCE || defined __CYGWIN__) && defined(CVAPI_EXPORTS)
#   define CV_EXPORTS __declspec(dllexport)
# elif defined __GNUC__ && __GNUC__ >= 4 && (defined(CVAPI_EXPORTS) || defined(__APPLE__))
#   define CV_EXPORTS __attribute__ ((visibility ("default")))
# endif
#endif

#ifdef _MSC_VER
#include <windows.h>
#include <io.h>
#include <BaseTsd.h>
typedef SIZE_T ssize_t;
#define C9_EXPORTS __declspec(dllexport)
#else
#include <termios.h>
#include <unistd.h>
#define C9_EXPORTS __attribute__ ((visibility ("default")))
#endif
