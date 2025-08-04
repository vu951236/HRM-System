package com.example.hrm.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    private ErrorCode errorCode;

    void setErrorCode(ErrorCode errorCode) {
      this.errorCode = errorCode;
    }
}
