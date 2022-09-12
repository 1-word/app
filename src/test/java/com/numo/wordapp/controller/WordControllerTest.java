package com.numo.wordapp.controller;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.Table;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)    //JUnit에 내장된 실행자 외에 다른 실행자 실행 (SpringRunner실행자 사용 연결자 역할)
@WebMvcTest(controllers = WordControllerTest.class) //스프링 테스트 어노테이션 (web에 집중 가능)
public class WordControllerTest {
    @Autowired  //빈 주입..
    private MockMvc mvc; //웹 API테스트할 때 사용, (http get, post등에 대한 api 테스트 가능)

    @DisplayName("[API][GET]Word ControllerTest")
    @Test
    public void test() throws Exception{
        String data = "Hello, World!";
        mvc.perform(get("/test"))   //get 요청
                .andExpect(status().isOk()) //결과 검증.. isOk:: status가 200인지 아닌지 검증
                .andExpect(content().string(data)); //응답 본분의 내용 검증
    }
}
