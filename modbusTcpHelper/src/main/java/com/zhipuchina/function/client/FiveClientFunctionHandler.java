package com.zhipuchina.function.client;

/**
 * 写多个寄存器
 * 请求PDU
 * 起始地址   2个字节
 * 寄存器数量  2个字节
 *  字节数   1个字节  N=寄存器数量*2    ADU[5]
 * 寄存器值   N个字节
 */
public class FiveClientFunctionHandler extends SixteenClientFunctionHandler {

}
