package com.miaoshaproject.error;

/**
 * @author xyfWritor
 * @create 2022-03-08 8:43
 */
public interface CommonError {
    public int getErrCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);
}
