package com.pudding.common.exception;

public interface BaseErrorInfoInterface {

    /**
     * 错误码
     * @return
     */
    String getResultCode();

    /**
     * 错误描述
     * @return
     */
    String getResultMsg();


}
