package com.DecodEat.global.exception;

import com.DecodEat.global.apiPayload.code.BaseErrorCode;
import com.DecodEat.global.apiPayload.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException{
    private final BaseErrorCode code;

    public ErrorReasonDto getErrorReasonDto() {
        return code.getReason();
    }

    public ErrorReasonDto getErrorReasonHttpStatus() {
        return code.getReasonHttpStatus();
    }
}
