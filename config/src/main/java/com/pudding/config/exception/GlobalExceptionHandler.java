package com.pudding.config.exception;

import com.pudding.common.dto.ApiResponse;
import com.pudding.common.enums.ResultCodeEnum;
import com.pudding.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;


/**
 * 自定义异常处理控制器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<String> businessException(BusinessException e) {
        log.error("[ 发生业务异常 ]",e);
        return ApiResponse.error(e.getErrorCode(),e.getMessage());
    }

    /**
     * 请求方法与接口方法不匹配异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponse<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("[ 错误请求类型 ]",e);
        return ApiResponse.error(ResultCodeEnum.HTTP_METHOD_ERROR);
    }

    /**
     * 请求格式错误异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("[ 非法的请求体 ]",e);
        return ApiResponse.error(ResultCodeEnum.INVALID_REQUEST_BODY);
    }

    /**
     * URL类型错误异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public ApiResponse<String> handleBindException(BindException e) {
        log.error("[ URL中的参数类型错误 ]", e);
        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMsg = null;
        if (fieldError != null) {
            errorMsg = fieldError.getDefaultMessage();
            if (errorMsg != null && errorMsg.contains("java.lang.NumberFormatException")) {
                errorMsg = fieldError.getField() + "参数类型错误";
            }
        }
        if (errorMsg != null && !"".equals(errorMsg)) {
            return ApiResponse.error(ResultCodeEnum.PATH_VARIABLE_ERROR.getResultCode(), errorMsg);
        }
        return ApiResponse.error(ResultCodeEnum.PATH_VARIABLE_ERROR);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ApiResponse<String> handle(MissingServletRequestParameterException e) {
        log.error("[  请求缺少必填的参数 ]", e);
        String errorMsg = null;
        String parameterName = e.getParameterName();
        if (!"".equals(parameterName)) {
            errorMsg = parameterName + "不能为空";
        }
        if (errorMsg != null) {
            return ApiResponse.error(ResultCodeEnum.REQUEST_PARAM_REQUIRED_ERROR.getResultCode(), errorMsg);
        }
        return ApiResponse.error(ResultCodeEnum.REQUEST_PARAM_REQUIRED_ERROR);
    }

    /**
     * Validated 异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[ Validated参数验证失败 ]",e);
        return ApiResponse.error(ResultCodeEnum.VALIDATE_ERROR.getResultCode(),e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 唯一索引冲突异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ApiResponse<String> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        log.error("[ 唯一索引冲突 ]",e);
        return ApiResponse.error(ResultCodeEnum.SQL_CONSTRAINT_ERROR);
    }

    /**
     * 数据库异常处理
     * @param e
     * @return
     */
    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ApiResponse<String> handleSQLSyntaxErrorException(Exception e) {
        log.error("[ 数据库异常 ]",e);
        return ApiResponse.error(ResultCodeEnum.DATABASE_ERROR);
    }


    /**
     * 其他异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleException(Exception e) {
        log.error("[ 系统出现未知错误 ]",e);
        return ApiResponse.error("系统错误: {}",e.getMessage());
    }

}
