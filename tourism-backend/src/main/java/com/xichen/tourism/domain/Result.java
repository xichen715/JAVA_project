package com.xichen.tourism.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xichen.tourism.enums.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {

    private boolean success;
    private String message;
    private Integer code;
    private Object data;
    private String msg;

    public Result(ResultCode code) {
        this.success = code.isSuccess();
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    public Result(ResultCode code, String message) {
        this.success = code.isSuccess();
        this.code = code.getCode();
        this.message = message;
    }

    public Result(ResultCode code, Object data) {
        this.success = code.isSuccess();
        this.code = code.getCode();
        this.message = code.getMessage();
        this.data = data;
    }

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = (code == 200 || code == 1000); // 或按你自定义规则
    }

    public static Result success() {
        return new Result(ResultCode.COMMON_SUCCESS);
    }

    public static Result success(Object data) {
        return new Result(ResultCode.COMMON_SUCCESS, data);
    }

    public static Result success(String msg) {
        return new Result(ResultCode.COMMON_SUCCESS, msg);
    }

    public static Result success(int code, String msg) {
        return new Result(code, msg, null);
    }

    public static Result alert(ResultCode code) {
        return new Result(code);
    }

    public static Result alert(ResultCode code, String message) {
        return new Result(code, message);
    }

    public static Result alert(ResultCode code, Exception e) {
        log.error(e.getMessage(), e);
        return new Result(code);
    }

    public static Result alert(Integer code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static Result fail() {
        return new Result(ResultCode.COMMON_FAIL);
    }

    public static Result fail(String msg) {
        return new Result(ResultCode.COMMON_FAIL, msg);
    }

    public static Result fail(String msg, Exception e) {
        log.error(e.getMessage(), e);
        return new Result(ResultCode.COMMON_FAIL, msg);
    }

}