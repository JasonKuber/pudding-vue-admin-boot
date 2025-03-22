package com.pudding.common.exception;

import lombok.Data;

/**
 * 自定义业务异常
 */
@Data
public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    public BusinessException(String errorCode, String errorMsg) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(BaseErrorInfoInterface baseErrorInfoInterface) {
        super(baseErrorInfoInterface.getResultMsg());
        this.errorCode = baseErrorInfoInterface.getResultCode();
        this.errorMsg = baseErrorInfoInterface.getResultMsg();
    }

    public BusinessException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public BusinessException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

}
