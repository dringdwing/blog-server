package com.vector.exception;

import com.vector.domain.enums.AppHttpCodeEnum;

/**
 * @description:
 * @Title: SystemException
 * @Package com.vector.exception
 * @Author 芝士汉堡
 * @Date 2022/9/19 12:31
 */
public class SystemException extends RuntimeException{


    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }

}
