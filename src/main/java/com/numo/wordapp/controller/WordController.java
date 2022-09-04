package com.numo.wordapp.controller;

import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.model.Word;
import com.numo.wordapp.service.SynonymService;
import com.numo.wordapp.service.WordService;
import com.numo.wordapp.service.impl.WordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class WordController{
    private final WordService wordService;
    private final SynonymService synonymService;

    public WordController(WordService wordService, SynonymService synonymService){
        this.wordService = wordService;
        this.synonymService = synonymService;
    }

    @RequestMapping(value = "/search/{data}", method = RequestMethod.GET)
    public List<WordDto.Response> getSearchWord(@PathVariable("data") String data){
        //WordDto.Response dto = new WordDto.Response(wordService.getBySearchWord(data));
        List<WordDto.Response> dto = null;
        System.out.println(data);
        if(data.equals("all")) {
            dto =  wordService.getByAllWord().stream()
                    .map(WordDto.Response::new)
                    .collect(Collectors.toList());
        }else{
            dto = wordService.getBySearchWord(data).stream()
                    .map(WordDto.Response::new)
                    .collect(Collectors.toList());
        }
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
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String setSaveWord(@RequestBody  WordDto.Request dto){
        String data = wordService.setByWord(dto);
        return data;
    }

    @PutMapping(value = "/update/{id}")
    public String setUpdateWord(@PathVariable("id")  int id, @RequestBody WordDto.Request dto){
        dto.setWord_id(id);
        String data = wordService.updateByWord(dto);
        synonymService.updateBySynonym(dto.getSynonyms());
        return data;
    }

    @DeleteMapping(value = "/remove/{id}")
    public String setRemoveWord(@PathVariable("id") int id) {
        String data = wordService.removeByWord(id);
        return data;
    }
}
