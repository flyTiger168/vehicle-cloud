package com.wenjie.cloud.common.exception;

import lombok.Getter;

/**
 * 业务异常
 * <p>
 * 所有可预期的业务错误均应抛出此异常，由 {@link GlobalExceptionHandler} 统一捕获并转换为 ApiResponse。
 * </p>
 */
@Getter
public class BusinessException extends RuntimeException {

    /** 业务错误码 */
    private final int errorCode;

    public BusinessException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
