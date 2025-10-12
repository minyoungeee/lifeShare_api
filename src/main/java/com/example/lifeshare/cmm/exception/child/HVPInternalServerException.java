package com.example.lifeshare.cmm.exception.child;

import com.example.lifeshare.cmm.code.HVPReponseCode;
import com.example.lifeshare.cmm.exception.HVPException;

public class HVPInternalServerException extends HVPException {
    public HVPInternalServerException(HVPReponseCode code) {
        super(code);
    }
}
