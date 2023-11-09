package com.numo.wordapp.test;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayListTest {
    public static class Data{
        String name;
        public Data(String name){
            this.name = name;
        }
    }

    @DisplayName("ArrayList 삭제 테스트")
    @Test
    public void test(){
        Data data = new Data("Hello");
        Data data2 = new Data("Hello2");

        List<Data> dataList = new ArrayList<>();
        dataList.add(data);
        dataList.add(data2);

        // stream map을 이용한 데이터 수정 테스트
        List<Data> dataMap = dataList.stream().map(data1->new Data(data1.name+"123")).collect(Collectors.toList());
//        dataList.remove(data2);
        System.out.println(dataList);
    }
}
