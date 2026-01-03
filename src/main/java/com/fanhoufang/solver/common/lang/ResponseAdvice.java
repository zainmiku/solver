package com.fanhoufang.solver.common.lang;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;


@RequiredArgsConstructor // 为所有final字段生成构造器注入
@RestControllerAdvice(basePackages = "com.fanhoufang.solver")
public class ResponseAdvice implements ResponseBodyAdvice<Object> {


	private final ObjectMapper objectMapper;
    /**
     * 是否开启功能 true：是 
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
     * 处理返回结果
     */
    @Override
    public Object beforeBodyWrite(@Nullable Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        //处理字符串类型数据，如果Controller返回Result的话，SpringBoot是直接返回.
        if(o instanceof Result){
            return o;
        }

	    //处理字符串类型数据，如果Controller返回String的话,进行处理
	    if (o instanceof String msg) {
		    try {
			    return objectMapper.writeValueAsString(Result.success(msg));
		    } catch (JacksonException e) {
			    e.printStackTrace();
		    }
	    }
        return Result.success(o);
    }
}