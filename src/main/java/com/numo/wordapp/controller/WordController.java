package com.numo.wordapp.controller;

import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.model.Word;
import com.numo.wordapp.service.WordService;
import com.numo.wordapp.service.impl.WordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WordController {
    //Controller

    @RequestMapping
    @GetMapping("/test")
    public String test(){
        String data = "Hello, World!!!";
        return data;
    }

    //Rest Controller
    @RestController
    public class WordRestController{
        @Autowired
        WordService wordService;

        @RequestMapping(value = "/save", method = RequestMethod.POST)
        public String setSaveWord(@RequestBody  WordDto.Request dto){
            String data = wordService.setByWord(dto);
            return data;
        }

        @RequestMapping(value = "/search", method = RequestMethod.GET)
        public WordDto.Response getSearchWord(int word_id){
            WordDto.Response dto = new WordDto.Response(wordService.getBySearchWord(word_id));
            return dto;
        }


        @RequestMapping(value = "/read", method = RequestMethod.GET)
        public List<WordDto.Response> getAllWord(){
            //DTO를 이용하여 무한 참조 방지.
            //stream을 이용하여 List<Word> 자료형을 List<WordDto.Response>로 변환
            List<WordDto.Response> dto =  wordService.getByAllWord().stream()
                    .map(WordDto.Response::new)
                    .collect(Collectors.toList());
            return dto;
           // return wordService.getByAllword();
        }

        @PutMapping(value = "/update/{id}")
        public String setUpdateWord(@PathVariable("id")  int id, @RequestBody WordDto.Request dto){
            dto.setWord_id(id);
            String data = wordService.updateByWord(dto);
            return data;
        }

        @DeleteMapping(value = "/remove/{id}")
        public String setRemoveWord(@PathVariable("id") int id){

            String data = wordService.removeByWord(id);
            return data;
        }

        @RequestMapping(value = "/Word", method = RequestMethod.GET)
        public List<Word> getValue2(){
            return   wordService.getByAllWord();
            // return wordService.getByAllword();
        }
    }
}
