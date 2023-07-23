package com.numo.wordapp.controller;

import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.model.ListResult;
import com.numo.wordapp.model.SingleResult;
import com.numo.wordapp.model.Word;
import com.numo.wordapp.service.ResponseService;
import com.numo.wordapp.service.WordService;
import com.numo.wordapp.service.impl.WordServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/word")
public class WordController{
    private final WordService wordService;
    private final ResponseService responseService;

    private String user_id;

    public WordController(WordService wordService,
                          ResponseService responseService){
        this.wordService = wordService;
        this.responseService = responseService;
    }

    @RequestMapping(value = "/search/{type}", method = RequestMethod.GET)
    public ListResult<WordDto.Response> getSearchWord(@PathVariable("type") String type){
        user_id = getUserId();
        List<Word> dto = null;
        if(type.equals("all")) {
            dto =  wordService.getByAllWord(user_id);
        }else{
            dto = wordService.getBySearchWord(user_id, type);
        }
        List<WordDto.Response> res = dto.stream().map(WordDto.Response::new)
                .collect(Collectors.toList());
        return responseService.getListResult(res);
    }

    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public ListResult<WordDto.Response> getAllWord(){
        user_id = getUserId();
        //DTO를 이용하여 무한 참조 방지.
        //stream을 이용하여 List<Word> 자료형을 List<WordDto.Response>로 변환
        List<WordDto.Response> dto =  wordService.getByAllWord(user_id).stream()
                .map(WordDto.Response::new)
                .collect(Collectors.toList());
        return responseService.getListResult(dto);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public SingleResult<WordDto.Response> setSaveWord(@RequestBody WordDto.Request dto){
        user_id = getUserId();
        dto.setUser_id(user_id);
        WordDto.Response wdr = new WordDto.Response(wordService.setByWord(dto));
        return responseService.getSingleResult(wdr);
    }

    @PutMapping(value = "/update/{type}/{id}")
    public SingleResult<String> setUpdateWord(@PathVariable("type") String type, @PathVariable("id")  int id, @RequestBody WordDto.Request dto){
        user_id = getUserId();
        dto.setWord_id(id);
        dto.setUser_id(user_id);
        String data = wordService.updateByWord(dto, type);
        return responseService.getSingleResult(data);
    }

    @DeleteMapping(value = "/remove/{id}")
    public SingleResult<String> setRemoveWord(@PathVariable("id") int id) {
        user_id = getUserId();
        WordDto.Request dto = new WordDto.Request();
        dto.setWord_id(id);
        dto.setUser_id(user_id);
        String data = wordService.removeByWord(dto);
        return responseService.getSingleResult(data);
    }

    private String getUserId(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
