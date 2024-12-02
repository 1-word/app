package com.numo.wordapp.comm.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/*
* 클래스명: JsonUtil
* 생성자 매개변수: 변환할 Object
* 기능: Object => Json으로 변환
* 작성일: 2022.06.29
* 작성자: 정현경
* */

public class JsonUtil {

    ObjectMapper mapper;

    public JsonUtil(){
       this.mapper = new ObjectMapper();
    }

    public String makeJson(Object obj){
        String json = "";
        try {
            //json 정렬
            json = mapper.enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T makeObject(String data, Class<T> classType){
        try {
            return new ObjectMapper().readValue(data, classType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getJson(Object obj) {
        return makeJson(obj);
    }
}
