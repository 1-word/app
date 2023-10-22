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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {web}/word에서 요청하는 CRUD 기능
 * @apiNote userId는 {@link com.numo.wordapp.comm.advice.WordAdvice} before()메서드에서 토큰의 id를 확인하여 파라미터의 가장 앞에 추가됨
 */
@RestController
@RequestMapping("/word")
public class WordController{
    private final WordService wordService;
    private final ResponseService responseService;
    private final FolderService folderService;

    public WordController(WordService wordService,
                          ResponseService responseService,
                          FolderService folderService){
        this.wordService = wordService;
        this.responseService = responseService;
        this.folderService = folderService;
    }

    @RequestMapping(value = "/search/{type}", method = RequestMethod.GET)
    public ListResult<WordDto.Response> getSearchWord(String userId, @PathVariable("type") String type){
        List<Word> dto = null;
        if(type.equals("all")) {
            dto =  wordService.getByAllWord(userId);
        }else{
            dto = wordService.getBySearchWord(userId, type);
        }
        List<WordDto.Response> res = dto.stream().map(WordDto.Response::new)
                .collect(Collectors.toList());
        return responseService.getListResult(res);
    }

    @GetMapping(value = {"/read/", "/read/{folder_id}" })
    public ListResult<Object> getWord(String userId, @PathVariable(required = false)Optional<Integer> folder_id){
        int folderId = folder_id.orElse(0);
        System.out.println(folderId);

        List<Word> res = null;
        if (folderId == 0){
            res = wordService.getByAllWord(userId);
        }else {
            res = wordService.getByFolderWord(userId, folderId);
        }
        List<WordDto.Response> dto = res.stream()
                .map(WordDto.Response::new)
                .collect(Collectors.toList());
        List<FolderDto.Response> fdto =  folderService.getByFolderName(userId).stream()
                .map(FolderDto.Response::new)
                .collect(Collectors.toList());
        HashMap<String, Object> datas = new HashMap<>();
        datas.put("folder", fdto);
        datas.put("word", dto);
        return responseService.getListResult(datas);
    }

    @RequestMapping(value = "/save/{type}", method = RequestMethod.POST)
    public SingleResult<WordDto.Response> setSaveWord(String userId, @PathVariable("type") String type, @RequestBody WordDto.Request dto){
        dto.setUser_id(userId);
        WordDto.Response wdr = new WordDto.Response(wordService.setByWord(dto, type));
        return responseService.getSingleResult(wdr);
    }

    @PutMapping(value = "/update/{type}/{id}")
    public SingleResult<String> setUpdateWord(String userId, @PathVariable("type") String type, @PathVariable("id")  int id, @RequestBody WordDto.Request dto){
        dto.setWord_id(id);
        dto.setUser_id(userId);
        String data = wordService.updateByWord(dto, type);
        return responseService.getSingleResult(data);
    }

    @DeleteMapping(value = "/remove/{id}")
    public SingleResult<String> setRemoveWord(String userId, @PathVariable("id") int id) {
        WordDto.Request dto = new WordDto.Request();
        dto.setWord_id(id);
        dto.setUser_id(userId);
        String data = wordService.removeByWord(dto);
        return responseService.getSingleResult(data);
    }

    @GetMapping(value = "/folder")
    public ListResult<FolderDto.Response> getFolderName(String userId){
        List<FolderDto.Response> fdto =  folderService.getByFolderName(userId).stream()
                .map(FolderDto.Response::new)
                .collect(Collectors.toList());
        return responseService.getListResult(fdto);
    }

    @PostMapping (value = "/folder")
    public SingleResult<Folder> setFolder(String userId, @RequestBody FolderDto.Request fdto){
        fdto.setUser_id(userId);
        Folder data = folderService.setByFolder(fdto);
        return responseService.getSingleResult(data);
    }

    @PutMapping (value = "/folder/{id}")
    public SingleResult<Folder> updateFolder(String userId, @PathVariable("id") int id, @RequestBody FolderDto.Request fdto){
        fdto.setFolder_id(id);
        fdto.setUser_id(userId);
        Folder data = folderService.updateByFolder(fdto);
        return responseService.getSingleResult(data);
    }

    @DeleteMapping(value="/folder/{id}")
    public SingleResult<String> removeFolder(String userId, @PathVariable("id") int id){
        FolderDto.Request fdto = new FolderDto.Request();
        fdto.setFolder_id(id);
        fdto.setUser_id(userId);
        String data = folderService.removeByFolder(fdto);
        return responseService.getSingleResult(data);
    }

}
