package com.ypy.matebackend.common.exception;

import com.ypy.matebackend.common.Resp;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = BusinessException.class)
    public Resp businessExceptionHandler(BusinessException e) {
        return Resp.error(e.getCode(), e.getDescription());
    }
}
