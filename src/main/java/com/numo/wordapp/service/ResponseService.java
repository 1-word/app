package com.numo.wordapp.service;

import com.numo.wordapp.model.CommonResult;
import com.numo.wordapp.model.ListResult;
import com.numo.wordapp.model.SingleResult;

import java.util.List;

public interface ResponseService {
    // 단일 결과 처리
    public <T> SingleResult<T> getSingleResult(T data);
    // 복수 결과 처리
    public <T> ListResult<T> getListResult(List<T> list);
    // 성공 결과 처리
    public CommonResult getSuccessResult();
    // 실패 결과 처리
    public CommonResult getFailResult();
}
