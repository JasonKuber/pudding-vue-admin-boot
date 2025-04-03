package com.pudding.common.vo;

import com.pudding.common.enums.ResultCodeEnum;
import com.pudding.common.exception.BaseErrorInfoInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse <T> {

    /**
     * 响应编码
     */
    private String code;

    /**
     * 成功消息
     */
    private String successMsg;

    /**
     * 响应结果
     */
    private T data;

    /**
     * 错误消息
     */
    private String errorMsg;

    public ApiResponse(BaseErrorInfoInterface baseErrorInfoInterface) {
        this.code = baseErrorInfoInterface.getResultCode();
        this.errorMsg = baseErrorInfoInterface.getResultMsg();
    }

    /**
     * 成功响应 自定义成功消息和响应结果
     * @param successMsg 成功消息
     * @param data 响应结果
     * @return 响应类
     * @param <T> 类型
     */
    public static <T> ApiResponse<T> success(String successMsg,T data){
        return new ApiResponse<>(ResultCodeEnum.SUCCESS.getResultCode(),
                successMsg,
                data,
                "");
    }

    /**
     * 成功响应 自定义响应结果
     * @param data 响应结果
     * @return 响应类
     * @param <T> 类型
     */
    public static <T>  ApiResponse<T> success(T data) {
        return success(ResultCodeEnum.SUCCESS.getResultMsg(),data);
    }

    /**
     * 成功响应不携带任何参数
     * @return 响应类
     * @param <T> 类型
     */
    public static <T> ApiResponse<T> success() {
        return success(null);
    }




    /**
     * 错误响应 自定义响应码和错误消息
     * @param code 响应码
     * @param errorMsg 错误消息
     * @return 响应类
     * @param <T> 类型
     */
    public static <T> ApiResponse<T> error(String code, String errorMsg) {
        return new ApiResponse<>(code,"",null,errorMsg);
    }

    /**
     * 错误响应 使用响应枚举类
     * @param resultCodeEnum 响应枚举类
     * @return 响应类
     * @param <T> 类型
     */
    public static <T> ApiResponse<T> error(ResultCodeEnum resultCodeEnum) {
        return error(resultCodeEnum.getResultCode(),resultCodeEnum.getResultMsg());
    }

    /**
     * 错误响应 自定义错误消息
     * @param errorMsg 错误消息
     * @return 响应类
     * @param <T> 类型
     */
    public static <T> ApiResponse<T> error(String errorMsg) {
        return new ApiResponse<>(ResultCodeEnum.SYS_ERROR.getResultCode(),
                "",
                null,
                errorMsg);
    }

    /**
     * 错误响应 不自定义任何消息和编码
     * @return 响应类
     * @param <T> 类型
     */
    public static <T> ApiResponse<T> error() {
        return error(ResultCodeEnum.SYS_ERROR.getResultMsg());
    }





}
