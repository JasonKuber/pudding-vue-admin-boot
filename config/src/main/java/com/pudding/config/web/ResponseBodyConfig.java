package com.pudding.config.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pudding.common.vo.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;

/**
 * 设置全局统一返回
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.pudding.api")
public class ResponseBodyConfig implements ResponseBodyAdvice<Object> {
    @Resource
    private ObjectMapper objectMapper;

    /**
     * supports方法要返回为true才会执行beforeBodyWrite方法
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果返回值类型为ApiResponse 或者 void 直接返回false
        return !returnType.getParameterType().equals(ApiResponse.class) ||
                !returnType.getParameterType().equals(Void.class);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        // 判断是否为String, 需要手动转换为JSON字符串
        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(ApiResponse.success(body));
            }catch (JsonProcessingException e) {
                log.error("[ ResponseBodyConfig String转换JSON异常 ]",e);
                return ApiResponse.error();
            }
        }


        return ApiResponse.success(body);
    }
}
