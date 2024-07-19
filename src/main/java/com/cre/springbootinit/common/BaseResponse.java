package com.cre.springbootinit.common;

import com.cre.springbootinit.constant.MessageConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 后端统一返回结果
 *
 * @param <T>
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseResponse<T> implements Serializable {


    private Integer code; //编码：0成功，1和其它数字为失败
    private String msg; //信息
    private T data; //数据

    public static <T> BaseResponse<T> success() {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.code = 0;
        baseResponse.msg = MessageConstant.SUCCESS;
        return baseResponse;
    }

    public static <T> BaseResponse<T> success(T object) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.data = object;
        baseResponse.code = 0;
        baseResponse.msg = MessageConstant.SUCCESS;
        return baseResponse;
    }


    public static <T> BaseResponse<T> error(int code, String message) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.msg = message;
        baseResponse.code = code;
        return baseResponse;
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.msg = message;
        baseResponse.code = errorCode.getCode();
        return baseResponse;
    }

}
