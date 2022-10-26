package com.zhipuchina.utils;

import com.zhipuchina.function.*;

public class FunctionControllerUtil {

    public static FunctionController createDefaultServerFunctionController(int functionCode){
        FunctionController functionController = null;
        switch (functionCode){
            case 1: functionController = new OneFunctionHandler();break;
            case 2: functionController = new TwoFunctionHandler();break;
            case 3: functionController = new ThreeFunctionHandler();break;
            case 4: functionController = new FourFunctionHandler();break;
            case 5: functionController = new FiveFunctionController();break;
            case 6: functionController = new SixFunctionController();break;
            case 15: functionController = new FifteenFunctionHandler();break;
            case 16: functionController = new SixteenFunctionController();break;
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
//            case 16: functionController = new SixteenFunctionController();break;
        }
        return functionController;
    }

}
