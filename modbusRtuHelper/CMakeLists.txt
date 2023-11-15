cmake_minimum_required(VERSION 3.16.3)
project(modbusRtuHelper VERSION 1.0.0)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_C_STANDARD 11)

find_package(JNI)

if (JNI_FOUND)
    message (STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
    message (STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
    include_directories("${JNI_INCLUDE_DIRS}")
#    LINK_DIRECTORIES(${JNI_LIBRARIES})
endif()


AUX_SOURCE_DIRECTORY(${PROJECT_SOURCE_DIR}/src ROOT_SRC)
INCLUDE_DIRECTORIES(${PROJECT_SOURCE_DIR}/include)

if ("${CMAKE_CXX_COMPILER_ID}" STREQUAL "GNU")
    if(${CMAKE_BUILD_TYPE} STREQUAL Debug)
        set(CMAKE_CXX_FLAGS "$ENV{CXXFLAGS} -rdynamic -O0 -g -Wall -Wno-deprecated -Werror -Wno-unused-function")
    else()
        set(CMAKE_CXX_FLAGS "$ENV{CXXFLAGS} -rdynamic -O3 -g -Wall -Wno-deprecated -Werror -Wno-unused-function")
    endif()
elseif("${CMAKE_CXX_COMPILER_ID}" STREQUAL "MSVC")
    #add_definitions(-DUNICODE -D_UNICODE)
    add_compile_options("$<$<C_COMPILER_ID:MSVC>:/utf-8>")
    add_compile_options("$<$<CXX_COMPILER_ID:MSVC>:/utf-8>")
    add_compile_options("$<$<CXX_COMPILER_ID:MSVC>:/EHsc>")
    set(CMAKE_CXX_FLAGS "$ENV{CXXFLAGS}")
    set (CMAKE_DEBUG_POSTFIX d)
endif()


SET(EXECUTABLE_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/bin)
SET(LIBRARY_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/bin)

#LINK_DIRECTORIES(${PROJECT_SOURCE_DIR}/lib)

add_library(modbusRtuHelper SHARED ${ROOT_SRC})

add_library(serialPort SHARED src/serialport.cpp)

add_executable(test-serialport test/test_serialport.cpp)
TARGET_LINK_LIBRARIES(test-serialport serialPort)


