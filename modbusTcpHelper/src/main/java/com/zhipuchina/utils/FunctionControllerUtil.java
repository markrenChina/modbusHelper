package com.zhipuchina.utils;

import com.zhipuchina.function.*;
import com.zhipuchina.function.client.*;

public class FunctionControllerUtil {

    public static FunctionController createDefaultServerFunctionController(int functionCode){
        FunctionController functionController = null;
        switch (functionCode){
            case 1: functionController = new OneServerFunctionReadHandler();break;
            case 2: functionController = new TwoServerFunctionReadHandler();break;
            case 3: functionController = new ThreeServerFunctionReadHandler();break;
            case 4: functionController = new FourServerFunctionReadHandler();break;
            case 5: functionController = new FiveServerFunctionHandler();break;
            case 6: functionController = new SixServerFunctionHandler();break;
            case 15: functionController = new FifteenServerFunctionHandler();break;
            case 16: functionController = new SixteenServerFunctionHandler();break;
        }
        return functionController;
    }

    public static FunctionController createDefaultClientFunctionController(int functionCode){
        FunctionController functionController = null;
        switch (functionCode){
            case 1: functionController = new OneClientFunctionHandler();break;
            case 2: functionController = new TwoClientFunctionHandler();break;
            case 3: functionController = new ThreeClientFunctionHandler();break;
            case 4: functionController = new FourClientFunctionHandler();break;
            case 5: functionController = new FiveClientFunctionHandler();break;
            case 6: functionController = new SixClientFunctionHandler();break;
            case 15: functionController = new FifteenClientFunctionHandler();break;
            case 16: functionController = new SixteenClientFunctionHandler();break;
        }
        return functionController;
    }

}
