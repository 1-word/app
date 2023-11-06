package com.numo.wordapp.service;

import com.numo.wordapp.comm.advice.exception.ErrorCode;
import com.numo.wordapp.model.response.CommonResult;
import com.numo.wordapp.model.response.ListResult;
import com.numo.wordapp.model.response.SingleResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface ResponseService {
    // 단일 결과 처리
    public <T> SingleResult<T> getSingleResult(T data);
    // 복수 결과 처리
    public <T> ListResult<T> getListResult(List<T> list);
    // 복수 결과 처리
    public <T> ListResult<T> getListResult(HashMap<String, T> datas);
    // 성공 결과 처리
    public CommonResult getSuccessResult();
    // 실패 결과 처리
    public CommonResult getFailResult();

    public CommonResult getFailResult(ErrorCode errorCode);

    public HttpServletResponse setResponseError(HttpServletResponse response, int status, ErrorCode errorCode) throws IOException;
}
