package com.pudding.common.enums;

import com.pudding.common.exception.BaseErrorInfoInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCodeEnum implements BaseErrorInfoInterface {

    SUCCESS("200","成功!"),
    HTTP_METHOD_ERROR("405","请求类型错误!"),

    VALIDATE_ERROR("400","参数校验错误!" ),
    INVALID_REQUEST_BODY("400", "请求数据格式有误，请检查后再试"),
    PATH_VARIABLE_ERROR("400", "URL中的参数类型错误"),
    REQUEST_PARAM_REQUIRED_ERROR("400","请求缺少必要参数"),




    SYS_ERROR("500","服务器内部错误!"),
    DATABASE_ERROR("500","数据库异常"),
    SQL_CONSTRAINT_ERROR("501", "唯一索引冲突"),
    ;


    /**
     * 响应码
     */
    private String resultCode;

    /**
     * 响应消息
     */
    private String resultMsg;

    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }


}
