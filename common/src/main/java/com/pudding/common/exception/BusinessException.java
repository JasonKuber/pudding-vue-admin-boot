package com.pudding.common.exception;

import com.pudding.common.utils.I18nUtils;
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
        super(I18nUtils.getMessage(baseErrorInfoInterface.getResultKey()));
        this.errorCode = baseErrorInfoInterface.getResultCode();
        this.errorMsg = I18nUtils.getMessage(baseErrorInfoInterface.getResultKey());
    }

    public BusinessException(String errorMsg) {
        super(errorMsg);
        this.errorCode = "500";
        this.errorMsg = errorMsg;
    }

    public BusinessException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

}
