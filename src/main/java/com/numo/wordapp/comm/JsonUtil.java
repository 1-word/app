package com.numo.wordapp.comm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.numo.wordapp.model.Word;

/*
* 클래스명: JsonUtil
* 생성자 매개변수: 변환할 Object
* 기능: Object => Json으로 변환
* 작성일: 2022.06.29
* 작성자: 정현경
* */

public class JsonUtil {

    ObjectMapper mapper = new ObjectMapper();
    String json = "";

    public JsonUtil(Object obj){
        makeJson(obj);
    }

    public void makeJson(Object obj){
        try {
            //json = mapper.writeValueAsString(obj);
            //json 정렬
            json = mapper.enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void makeObject(Class c){
        Class<?> c1 = c;

    }

    public String getJson() {
        return json;
    }
}
