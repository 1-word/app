package com.numo.wordapp.controller;

import com.numo.wordapp.dto.FolderDto;
import com.numo.wordapp.dto.PageDto;
import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.model.word.Folder;
import com.numo.wordapp.model.response.ListResult;
import com.numo.wordapp.model.response.SingleResult;
import com.numo.wordapp.model.word.Word;
import com.numo.wordapp.service.FolderService;
import com.numo.wordapp.service.ResponseService;
import com.numo.wordapp.service.WordService;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = {"/search/{text}","/search/{folder_id}/{text}"}, method = RequestMethod.GET)
    public ListResult<Object> getSearchWord(String userId,
                                            @PathVariable(required = false, name = "folder_id")Optional<Integer> folder_id,
                                            @PathVariable("text") String searchText,
                                            @RequestParam(required = false, name = "page") Optional<Integer> page,
                                            @RequestParam(required = false, name = "last_wid") Optional<Integer> last_wid,
                                            @RequestParam(required = false, name = "memorization") Optional<String> memorization,
                                            @RequestParam(required = false, name = "language") Optional<String> language){
        int folderId = folder_id.orElse(-1);
        int lastWid = last_wid.orElse(-1);
        int curPage = page.orElse(0);
        String memoStatus = memorization.orElse("");
        String lang = language.orElse("");

        WordDto.Read readDto = WordDto.Read.builder()
                .user_id(userId)
                .folder_id(folderId)
                .last_word_id(lastWid)
                .page(curPage)
                .memorization(memoStatus)
                .language(lang)
                .search_text(searchText).build();

        Slice<Word> pageWord = wordService.getBySearchWord(readDto);
        List<WordDto.Response> dto = pageWord.getContent().stream().map(WordDto.Response::new).collect(Collectors.toList());

        HashMap<String, Object> datas = new HashMap<>();
        datas.put("word", dto);
        datas.put("page", PageDto.getPageInstance(pageWord, dto));
        return responseService.getListResult(datas);
    }

    @GetMapping(value = {"/read", "/read/{folder_id}" })
    public ListResult<Object> getPagingWord(String userId,
                                            @PathVariable(required = false) Optional<Integer> folder_id,
                                            @RequestParam(required = false, name = "page") Optional<Integer> page,
                                            @RequestParam(required = false, name = "last_wid") Optional<Integer> last_wid,
                                            @RequestParam(required = false, name = "memorization") Optional<String> memorization,
                                            @RequestParam(required = false, name = "language") Optional<String> language){
        int folderId = folder_id.orElse(-1);
        int lastWid = last_wid.orElse(-1);
        int curPage = page.orElse(0);
        String memoStatus = memorization.orElse("");
        String lang = language.orElse("");
        System.out.println("lang" + lang);

        WordDto.Read readDto = WordDto.Read.builder()
                .user_id(userId)
                .folder_id(folderId)
                .last_word_id(lastWid)
                .memorization(memoStatus)
                .language(lang)
                .build();

        Slice<Word> pageWord = wordService.getByPagingWord(curPage, readDto);
        List<WordDto.Response> dto = pageWord.getContent().stream().map(WordDto.Response::new).collect(Collectors.toList());
        HashMap<String, Object> datas = new HashMap<>();

        datas.put("word", dto);

        //마지막에 조회한 단어 id값 찾기
        datas.put("page", PageDto.getPageInstance(pageWord, dto));
        //처음 조회하는 경우 폴더 관련 추가
        if (lastWid == -1) {
            List<FolderDto.Response> fdto =  folderService.getByFolderName(userId).stream()
                    .map(FolderDto.Response::new)
                    .collect(Collectors.toList());
            datas.put("folder", fdto);
        }
        return responseService.getListResult(datas);
    }

    @RequestMapping(value = "/{gttsType}", method = RequestMethod.POST)
    public SingleResult<WordDto.Response> setSaveWord(String userId, @PathVariable("gttsType") String gttsType, @RequestBody WordDto.Request dto){
        dto.setUser_id(userId);
        return responseService.getSingleResult(new WordDto.Response(wordService.saveWord(dto, gttsType)));
    }

    /**
     * 타입별 단어 데이터 업데이트
     * <pre>all: 모든 단어 데이터 업데이트</pre>
     * <pre>memorization: 암기 여부 업데이트</pre>
     * <pre>memo: 단어 메모 업데이트</pre>
     * <pre>wordFoler: 단어 폴더 변경</pre>
     * @param userId 로그인한 유저 아이디
     * @param type {@link com.numo.wordapp.service.impl.WordServiceImpl.UpdateType} all, memorization, memo, wordFoler
     * @param id 업데이트할 단어 아이디
     * @param dto 업데이트할 데이터
     * @return 업데이트한 단어 전체 데이터
     */
    @PutMapping(value = "/{type}/{id}")
    public SingleResult<WordDto.Response> setUpdateWord(String userId, @PathVariable("type") String type, @PathVariable("id")  int id, @RequestBody WordDto.Request dto){
        dto.setWord_id(id);
        dto.setUser_id(userId);
        WordDto.Response data = new WordDto.Response(wordService.updateByWord(dto, type));
        return responseService.getSingleResult(data);
    }

    @DeleteMapping(value = "/{id}")
    public SingleResult<Integer> setRemoveWord(String userId, @PathVariable("id") int id) {
        WordDto.Request dto = new WordDto.Request();
        dto.setWord_id(id);
        dto.setUser_id(userId);
        int data = wordService.removeByWord(dto);
        return responseService.getSingleResult(data);
    }

    /** 폴더 관련 **/
    @GetMapping(value = "/folder")
    public ListResult<FolderDto.Response> getFolderName(String userId){
        List<FolderDto.Response> fdto =  folderService.getByFolderName(userId).stream()
                .map(FolderDto.Response::new)
                .collect(Collectors.toList());
        return responseService.getListResult(fdto);
    }

    @PostMapping (value = "/folder")
    public SingleResult<FolderDto.Response> setFolder(String userId, @RequestBody FolderDto.Request fdto){
        fdto.setUser_id(userId);
        FolderDto.Response data = new FolderDto.Response(folderService.setByFolder(fdto));
        return responseService.getSingleResult(data);
    }

    /**
     * 폴더 데이터 변경
     * @param userId 로그인한 유저 아이디
     * @param id 폴더 아이디
     * @param fdto 폴더 이름, 폴더 색상, 글자 색상, 메모
     * @return 업데이트한 폴더 정보
     */
    @PutMapping (value = "/folder/{id}")
    public SingleResult<FolderDto.Response> updateFolder(String userId, @PathVariable("id") int id, @RequestBody FolderDto.Request fdto){
        fdto.setFolder_id(id);
        fdto.setUser_id(userId);
        FolderDto.Response data = new FolderDto.Response(folderService.updateByFolder(fdto));
        return responseService.getSingleResult(data);
    }

    @DeleteMapping(value="/folder/{id}")
    public SingleResult<Integer> removeFolder(String userId, @PathVariable("id") int id){
        FolderDto.Request fdto = new FolderDto.Request();
        fdto.setFolder_id(id);
        fdto.setUser_id(userId);
        int data = folderService.removeByFolder(fdto);
        return responseService.getSingleResult(data);
    }

}
