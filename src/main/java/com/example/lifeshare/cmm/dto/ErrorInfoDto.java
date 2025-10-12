package com.example.lifeshare.cmm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serializable;
@Data
@JsonPropertyOrder({ "code", "message", "cause" })
@SuppressWarnings("serial")
public class ErrorInfoDto implements Serializable {
    private String code;
    private String message;
    private String cause;
    @JsonIgnore
    private Object[] args;
}