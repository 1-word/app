package com.numo.wordapp.controller;

import com.numo.wordapp.service.word.WordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WordControllerTest.class) //스프링 테스트 어노테이션 (web에 집중 가능)
public class WordControllerTest {
    @Autowired  //빈 주입..
    private MockMvc mvc; //웹 API테스트할 때 사용, (http get, post등에 대한 api 테스트 가능)
    @Autowired
    private WordService wordService;


    @DisplayName("[API][GET]Word ControllerTest")
    @Test
    public void test() throws Exception{
        String data = "Hello, World!";
        mvc.perform(get("/test"))   //get 요청
                .andExpect(status().isOk()) //결과 검증.. isOk:: status가 200인지 아닌지 검증
                .andExpect(content().string(data)); //응답 본분의 내용 검증
    }

    @DisplayName("[API][PUT]단어 데이터 저장 테스트")
    @Test
    public void wordSaveTest() throws Exception{

    }
}
