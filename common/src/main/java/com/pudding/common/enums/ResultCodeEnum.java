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

    SUCCESS("200","response.SUCCESS"),

    NOT_LOGIN("401","response.NOT_LOGIN"),
    TOKEN_INVALID("401","response.TOKEN_INVALID"),
    TOKEN_IP_NO_MATCH("401","response.TOKEN_IP_NO_MATCH"),



    VALIDATE_ERROR("400","response.VALIDATE_ERROR" ),
    INVALID_REQUEST_BODY("400", "response.INVALID_REQUEST_BODY"),
    PATH_VARIABLE_ERROR("400", "response.PATH_VARIABLE_ERROR"),
    REQUEST_PARAM_REQUIRED_ERROR("400","response.REQUEST_PARAM_REQUIRED_ERROR"),
    ACCOUNT_OR_PASSWORD_ERROR("400","response.ACCOUNT_OR_PASSWORD_ERROR"),
    USER_INFO_ERROR("400","response.USER_INFO_ERROR"),
    PARAMETERS_EXISTS("400","response.PARAMETERS_EXISTS"),




    LIMITED_ACCESS("403","response.LIMITED_ACCESS"),
    ACCOUNT_DISABLED("403","response.ACCOUNT_DISABLED"),


    HTTP_METHOD_ERROR("405","response.HTTP_METHOD_ERROR"),

    SYS_ERROR("500","response.SYS_ERROR"),
    DATABASE_ERROR("500","response.DATABASE_ERROR"),


    SQL_CONSTRAINT_ERROR("501", "response.SQL_CONSTRAINT_ERROR"),
    ;


    /**
     * 响应码
     */
    private String resultCode;

    /**
     * 响应消息
     */
    private String resultKey;

    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultKey() {
        return resultKey;
    }


}
