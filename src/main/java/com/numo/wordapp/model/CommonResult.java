package com.numo.wordapp.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.annotation.ApplicationScope;

@Getter
@Setter
public class CommonResult {

    @ApiModelProperty(value = "응답 성공 여부: T/F")
    private boolean success;

    @ApiModelProperty(value = "응답코드: >= 0 정상, <0 비정상")
    private int code;

    @ApiModelProperty(value = "응답 메시지")
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
