package com.numo.wordapp.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {

    @Schema(description = "응답 성공 여부: T/F")
    private boolean success;

    @Schema(description = "응답코드: >= 0 정상, <0 비정상")
    private int code;

    @Schema(description = "응답 메시지")
    private String msg;

    public CommonResult(){

    }

    @Builder
    public CommonResult(boolean success, int code, String msg){
        this.success = success;
        this.code = code;
        this.msg = msg;
    }
}
