package com.numo.api.comm.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {
    @Schema(description = "응답 성공 여부: T/F")
    private boolean success;

    @Schema(description = "응답코드: >= 0 정상, <0 비정상")
    private int code;

    @Schema(description = "응답 메시지")
    private String msg;

    @Schema(description = "데이터")
    private T data;

    public static CommonResult getSuccessResult() {
        return CommonResult.builder()
                .success(true)
                .code(CommonResponse.SUCCESS.getCode())
                .msg(CommonResponse.SUCCESS.getMsg())
                .build();
    }

    public static CommonResult getFailResult() {
        return CommonResult.builder()
                .success(false)
                .code(CommonResponse.FAIL.getCode())
                .msg(CommonResponse.FAIL.getMsg())
                .build();
    }

    private void setResult(boolean success, int code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }
}
