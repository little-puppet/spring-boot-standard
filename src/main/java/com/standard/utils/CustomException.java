package com.manage.contract.utils;

import org.apache.commons.lang3.StringUtils;

public class CustomException extends Exception{

    public final static int PathError = 70001;

    public final static int ParamLackError = 80001;
    public final static int ParamTypeError = 80002;

    public final static int ResultLackError = 90001;
    public final static int ResultTooManyError = 90002;

    //public final static int EncodeBase64Error = -09;
    //public final static int DecodeBase64Error = -40010;
    //public final static int GenReturnXmlError = -40011;

    private int code;

    private static String getMessage(int code, String message ) {
        String temp;
        switch (code) {
            case PathError:
                temp =  "接口路径不存在";

            case ParamLackError:
                temp =  "缺少参数";
                break;
            case ParamTypeError:
                temp =  "参数格式错误";
                break;

            case ResultLackError:
                temp =  "未查询到数据";
                break;
            case ResultTooManyError:
                temp =  "查询多条数据";
                break;
//		case EncodeBase64Error:
//			return "base64加密错误";
//		case DecodeBase64Error:
//			return "base64解密错误";
//		case GenReturnXmlError:
//			return "xml生成失败";
            default:
                temp = ""; // cannot be
        }
        if(StringUtils.isNotBlank(message)){
            return temp + " " + message;
        } else {
            return temp;
        }
    }

    public int getCode() {
        return code;
    }

    public CustomException(int code, String message) {
        super(getMessage(code, message));
        this.code = code;
    }
}
