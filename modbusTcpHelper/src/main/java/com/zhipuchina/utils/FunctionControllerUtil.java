package com.zhipuchina.utils;

import com.zhipuchina.function.*;
import com.zhipuchina.function.client.SixteenClientFunctionTemplate;
import com.zhipuchina.function.client.ThreeClientFunctionHandler;

public class FunctionControllerUtil {

    public static FunctionController createDefaultServerFunctionController(int functionCode){
        FunctionController functionController = null;
        switch (functionCode){
            case 1: functionController = new OneServerFunctionHandler();break;
            case 2: functionController = new TwoServerFunctionHandler();break;
            case 3: functionController = new ThreeServerFunctionHandler();break;
            case 4: functionController = new FourServerFunctionHandler();break;
            case 5: functionController = new FiveServerFunctionController();break;
            case 6: functionController = new SixServerFunctionController();break;
            case 15: functionController = new FifteenServerFunctionHandler();break;
            case 16: functionController = new SixteenServerFunctionController();break;
        }
        return functionController;
    }

    public static FunctionController createDefaultClientFunctionController(int functionCode){
        FunctionController functionController = null;
        switch (functionCode){
//            case 1: functionController = new OneFunctionHandler();break;
//            case 2: functionController = new TwoFunctionHandler();break;
            case 3: functionController = new ThreeClientFunctionHandler();break;
//            case 4: functionController = new FourFunctionHandler();break;
//            case 5: functionController = new FiveFunctionController();break;
//            case 6: functionController = new SixFunctionController();break;
//            case 15: functionController = new FifteenFunctionHandler();break;
            case 16: functionController = new SixteenClientFunctionTemplate();break;
        }
        return functionController;
    }

}
