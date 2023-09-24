package com.numo.wordapp.controller;

import com.numo.wordapp.dto.FolderDto;
import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.model.Folder;
import com.numo.wordapp.model.ListResult;
import com.numo.wordapp.model.SingleResult;
import com.numo.wordapp.model.Word;
import com.numo.wordapp.service.FolderService;
import com.numo.wordapp.service.ResponseService;
import com.numo.wordapp.service.WordService;
import com.numo.wordapp.service.impl.WordServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/word")
public class WordController{
    private final WordService wordService;
    private final ResponseService responseService;
    private final FolderService folderService;

    private String user_id;

    public WordController(WordService wordService,
                          ResponseService responseService,
                          FolderService folderService){
        this.wordService = wordService;
        this.responseService = responseService;
        this.folderService = folderService;
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

    @GetMapping(value = "/read")
    public ListResult<Object> getFolderNameAndAllWord(){
        user_id = getUserId();
        //DTO를 이용하여 무한 참조 방지.
        //stream을 이용하여 List<Word> 자료형을 List<WordDto.Response>로 변환
        List<WordDto.Response> dto = wordService.getByAllWord(user_id).stream()
                .map(WordDto.Response::new)
                .collect(Collectors.toList());
        List<FolderDto.Response> fdto =  folderService.getByFolderName(user_id).stream()
                .map(FolderDto.Response::new)
                .collect(Collectors.toList());
        HashMap<String, Object> datas = new HashMap<>();
        datas.put("folder", fdto);
        datas.put("word", dto);
        return responseService.getListResult(datas);
    }

    @GetMapping(value = "/read/{folderId}")
    public ListResult<Object> getFolderWord(@PathVariable("folderId") int folder_id){
        user_id = getUserId();
            //DTO를 이용하여 무한 참조 방지.
            //stream을 이용하여 List<Word> 자료형을 List<WordDto.Response>로 변환
        List<WordDto.Response> dto = wordService.getByFolderWord(user_id,folder_id).stream()
                    .map(WordDto.Response::new)
                    .collect(Collectors.toList());
        List<FolderDto.Response> fdto =  folderService.getByFolderName(user_id).stream()
                .map(FolderDto.Response::new)
                .collect(Collectors.toList());
        HashMap<String, Object> datas = new HashMap<>();
        datas.put("folder", fdto);
        datas.put("word", dto);
        return responseService.getListResult(datas);
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

    @GetMapping(value = "/folder")
    public ListResult<FolderDto.Response> getFolderName(){
        user_id = getUserId();
        List<FolderDto.Response> fdto =  folderService.getByFolderName(user_id).stream()
                .map(FolderDto.Response::new)
                .collect(Collectors.toList());
        return responseService.getListResult(fdto);
    }

    @PostMapping (value = "/folder")
    public SingleResult<Folder> setFolder(@RequestBody FolderDto.Request fdto){
        user_id = getUserId();
        fdto.setUser_id(user_id);
        Folder data = folderService.setByFolder(fdto);
        return responseService.getSingleResult(data);
    }

    @PutMapping (value = "/folder/{id}")
    public SingleResult<Folder> updateFolder(@PathVariable("id") int id, @RequestBody FolderDto.Request fdto){
        user_id = getUserId();
        fdto.setFolder_id(id);
        fdto.setUser_id(user_id);
        Folder data = folderService.updateByFolder(fdto);
        return responseService.getSingleResult(data);
    }

    @DeleteMapping(value="/folder/{id}")
    public SingleResult<String> removeFolder(@PathVariable("id") int id){
        user_id = getUserId();
        FolderDto.Request fdto = new FolderDto.Request();
        fdto.setFolder_id(id);
        fdto.setUser_id(user_id);
        String data = folderService.removeByFolder(fdto);
        return responseService.getSingleResult(data);
    }

    private String getUserId(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
