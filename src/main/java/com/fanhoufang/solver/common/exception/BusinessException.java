package com.fanhoufang.solver.common.exception;

import com.fanhoufang.solver.common.constant.ReturnCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException{


    private int code;

    private String errMsg;

    public  BusinessException(ReturnCode reeurnCode){
        this.code = reeurnCode.getCode();
        this.errMsg = reeurnCode.getMsg();
    }

        public  BusinessException(String message){
        this.code = ReturnCode.CD999.getCode();
        this.errMsg = message;
    }
}
