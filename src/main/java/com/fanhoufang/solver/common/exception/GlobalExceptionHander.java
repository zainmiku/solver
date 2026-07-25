package com.fanhoufang.solver.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fanhoufang.solver.common.lang.Result;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackages = "com.fanhoufang.solver.controller")
public class GlobalExceptionHander {



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<String> hander (MethodArgumentNotValidException e){
        log.error("实体类校验异常:-----------{}",e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return Result.fail(objectError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BusinessException.class)
    public Result<String> hander (BusinessException e){
        log.error("业务异常:-----------{}",e.getErrMsg());
        return Result.fail(e.getCode(),e.getErrMsg(),null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result<String> hander (RuntimeException e){
        log.error("运行异常:-----------{}",e);
        return Result.fail(e.getMessage());
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = Exception.class)
    public Result<String> hander (Exception e){
        log.error("其他异常:-----------{}",e);
        return Result.fail(e.getMessage());
    }
}
