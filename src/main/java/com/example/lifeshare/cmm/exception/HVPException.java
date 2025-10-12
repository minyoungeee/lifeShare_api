package com.example.lifeshare.cmm.exception;

import com.example.lifeshare.cmm.code.HVPReponseCode;
import com.example.lifeshare.cmm.dto.ErrorInfoDto;
import lombok.Getter;

public class HVPException extends RuntimeException {
    @Getter
    private ErrorInfoDto error;

    public HVPException(HVPReponseCode responseCode) {
        this(responseCode, null, (Object[]) null);
    }

    public HVPException(HVPReponseCode responseCode, Object... args) {
        this(responseCode, null, args);
    }

    public HVPException(HVPReponseCode responseCode, Throwable cause) {
        this(responseCode, cause, (Object[]) null);
    }

    public HVPException(HVPReponseCode responseCode, Throwable cause, Object... args) {
        super(responseCode.getCode(), cause);

        this.error = new ErrorInfoDto();
        this.error.setCode(responseCode.getCode());
        this.error.setMessage(responseCode.getMessage());
        this.error.setArgs(args);
    }
}
