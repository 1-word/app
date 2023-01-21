package com.numo.wordapp.controller;

import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.model.ListResult;
import com.numo.wordapp.model.SingleResult;
import com.numo.wordapp.service.ResponseService;
import com.numo.wordapp.service.WordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/word")
public class WordController{
    private final WordService wordService;
    private final ResponseService responseService;

    public WordController(WordService wordService,
                          ResponseService responseService){
        this.wordService = wordService;
        this.responseService = responseService;
    }

    @RequestMapping(value = "/search/{user_id}/{data}", method = RequestMethod.GET)
    public ListResult<WordDto.Response> getSearchWord(@PathVariable("user_id") String user_id, @PathVariable("data") String data){
        List<WordDto.Response> dto = null;
        System.out.println(data);
        if(data.equals("all")) {
            dto =  wordService.getByAllWord(user_id).stream()
                    .map(WordDto.Response::new)
                    .collect(Collectors.toList());
        }else{
            dto = wordService.getBySearchWord(user_id, data).stream()
                    .map(WordDto.Response::new)
                    .collect(Collectors.toList());
        }
        return responseService.getListResult(dto);
    }

    @RequestMapping(value = "/read/{user_id}", method = RequestMethod.GET)
    public ListResult<WordDto.Response> getAllWord(@PathVariable("user_id") String user_id){
        //DTO를 이용하여 무한 참조 방지.
        //stream을 이용하여 List<Word> 자료형을 List<WordDto.Response>로 변환
        List<WordDto.Response> dto =  wordService.getByAllWord(user_id).stream()
                .map(WordDto.Response::new)
                .collect(Collectors.toList());
        return responseService.getListResult(dto);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public SingleResult<String> setSaveWord(@RequestBody WordDto.Request dto){
        String data = wordService.setByWord(dto);
        return responseService.getSingleResult(data);
    }

    @PutMapping(value = "/update/{id}")
    public SingleResult<String> setUpdateWord(@PathVariable("id")  int id, @RequestBody WordDto.Request dto){
        dto.setWord_id(id);
        String data = wordService.updateByWord(dto);
        return responseService.getSingleResult(data);
    }

    @DeleteMapping(value = "/remove/{id}")
    public SingleResult<String> setRemoveWord(@PathVariable("id") int id) {
        String data = wordService.removeByWord(id);
        return responseService.getSingleResult(data);
    }
}
