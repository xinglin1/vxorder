package com.xmcc.exception;

import com.xmcc.commen.ResultEnums;
import lombok.Getter;

/**
 * @author 张兴林
 * @date 2019-04-17 14:30
 */
@Getter
public class CustomException extends RuntimeException {

    private int code;

    public CustomException() {
        super();
    }

    public CustomException(String message) {
        this(ResultEnums.FAIL.getCode(),message);
    }

    public CustomException(int code,String message) {
        super(message);
        this.code = code;
    }
}
