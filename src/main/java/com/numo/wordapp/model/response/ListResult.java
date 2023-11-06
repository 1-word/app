package com.numo.wordapp.model.response;

import java.util.HashMap;
import java.util.List;

public class ListResult<T> extends CommonResult {
    private List<T> list;
    private HashMap<String, T> datas;

    public void setList(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setDatas(HashMap<String, T> datas) {
        this.datas = datas;
    }

    public HashMap<String, T> getDatas() {
        return datas;
    }
}
