package com.cre.springbootinit.exception;


import com.cre.springbootinit.constant.MessageConstant;
import com.cre.springbootinit.pojo.common.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public BaseResponse handleException(Exception exception) {
        log.error("系统异常：", exception);
        return BaseResponse.error(StringUtils.hasLength(exception.getMessage()) ? exception.getMessage() : MessageConstant.FAIL);

    }
}
