package com.DecodEat.global.exception.handler;

import com.DecodEat.global.apiPayload.code.BaseErrorCode;
import com.DecodEat.global.exception.GeneralException;

public class GlobalHandler extends GeneralException {
    public GlobalHandler(BaseErrorCode code) {
        super(code);
    }
}
