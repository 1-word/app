package com.numo.wordapp.controller;

import com.numo.wordapp.model.Word;
import com.numo.wordapp.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class WordController {
    //Controller
    @GetMapping("word")
    public String word(){
        String data = "Hello, World!";
        return data;
        //return "home";
    }

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

        @RequestMapping(value = "/restWord", method = RequestMethod.GET)
        public String saveValue(){
        //public Map<String, Object> getValue(){
        //public @ResponseBody word getValue(){

            String _word = "だんかいよそく";
            String _mean = "단계 예측";
            String _wread = "btlz";
            String _memo = "test";

            String data = wordService.setByWord(_word, _mean, _wread, _memo);

            //Map<String, Object> map = wordRepo.findAll();
            //Map<String, Object> map = wordService.getByAllWord();
            //List<word> list = wordRepo.findAll();
            //System.out.println(list.get(0).getMean());
            //return map;
            //String data = "value";
            return data;
        }

        @RequestMapping(value = "/tWord", method = RequestMethod.GET)
        public @ResponseBody Word getValue(){
            Word data = wordService.getBySearchWord(1);
            return data;
        }

        @RequestMapping(value = "/Word", method = RequestMethod.GET)
        public List<Word> getValue1(){
           List<Word> data = wordService.getByAllWord();
           return data;
        }
    }
}
