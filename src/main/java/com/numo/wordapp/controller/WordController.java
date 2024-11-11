package com.numo.wordapp.controller;

import com.numo.wordapp.aop.WordAspect;
import com.numo.wordapp.dto.word.FolderDto;
import com.numo.wordapp.dto.word.PageDto;
import com.numo.wordapp.dto.word.WordDto;
import com.numo.wordapp.entity.word.Word;
import com.numo.wordapp.security.service.UserDetailsImpl;
import com.numo.wordapp.service.word.FolderService;
import com.numo.wordapp.service.word.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {web}/word에서 요청하는 CRUD 기능
 * @apiNote userId는 {@link WordAspect} before()메서드에서 토큰의 id를 확인하여 파라미터의 가장 앞에 추가됨
 */

@RestController
@RequestMapping("/word")
@RequiredArgsConstructor
public class WordController{
    private final WordService wordService;
    private final FolderService folderService;

    // TODO 파라미터 정리 (RequestParam을 DTO로 정리)
    @RequestMapping(value = {"/search/{text}","/search/{folder_id}/{text}"}, method = RequestMethod.GET)
    public ResponseEntity<Object> getSearchWord(@AuthenticationPrincipal UserDetailsImpl user,
                                                @PathVariable(required = false, name = "folder_id")Optional<Integer> folder_id,
                                                @PathVariable("text") String searchText,
                                                @RequestParam(required = false, name = "page") Optional<Integer> page,
                                                @RequestParam(required = false, name = "last_wid") Optional<Integer> last_wid,
                                                @RequestParam(required = false, name = "memorization") Optional<String> memorization,
                                                @RequestParam(required = false, name = "language") Optional<String> language){
        String userId = user.getUsername();
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

        // TODO DTO로 처리하도록 수정
        List<WordDto.Response> dto = pageWord.getContent().stream().map(WordDto.Response::new).collect(Collectors.toList());

        HashMap<String, Object> datas = new HashMap<>();
        datas.put("word", dto);
        datas.put("page", PageDto.getPageInstance(pageWord, dto));
        return ResponseEntity.ok(datas);
    }

    @GetMapping(value = {"/read", "/read/{folder_id}" })
    public ResponseEntity<Object> getPagingWord(@AuthenticationPrincipal UserDetailsImpl user,
                                            @PathVariable(required = false) Optional<Integer> folder_id,
                                            @RequestParam(required = false, name = "page") Optional<Integer> page,
                                            @RequestParam(required = false, name = "last_wid") Optional<Integer> last_wid,
                                            @RequestParam(required = false, name = "memorization") Optional<String> memorization,
                                            @RequestParam(required = false, name = "language") Optional<String> language){
        String userId = user.getUsername();
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
        return ResponseEntity.ok(datas);
    }

    @RequestMapping(value = "/{gttsType}", method = RequestMethod.POST)
    public ResponseEntity<WordDto.Response> setSaveWord(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("gttsType") String gttsType, @RequestBody WordDto.Request dto){
        String userId = user.getUsername();
        dto.setUser_id(userId);
        return ResponseEntity.ok(new WordDto.Response(wordService.saveWord(dto, gttsType)));
    }

    /**
     * 타입별 단어 데이터 업데이트
     * <pre>all: 모든 단어 데이터 업데이트</pre>
     * <pre>memorization: 암기 여부 업데이트</pre>
     * <pre>memo: 단어 메모 업데이트</pre>
     * <pre>wordFoler: 단어 폴더 변경</pre>
     * @param userId 로그인한 유저 아이디
     * @param type {@link com.numo.wordapp.service.word.impl.WordServiceImpl.UpdateType} all, memorization, memo, wordFoler
     * @param id 업데이트할 단어 아이디
     * @param dto 업데이트할 데이터
     * @return 업데이트한 단어 전체 데이터
     */
    @PutMapping(value = "/{type}/{id}")
    public ResponseEntity<WordDto.Response> setUpdateWord(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("type") String type, @PathVariable("id")  int id, @RequestBody WordDto.Request dto){
        String userId = user.getUsername();
        dto.setWord_id(id);
        dto.setUser_id(userId);
        WordDto.Response data = new WordDto.Response(wordService.updateByWord(dto, type));
        return ResponseEntity.ok(data);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Integer> setRemoveWord(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("id") int id) {
        String userId = user.getUsername();
        WordDto.Request dto = new WordDto.Request();
        dto.setWord_id(id);
        dto.setUser_id(userId);
        int data = wordService.removeByWord(dto);
        return ResponseEntity.ok(data);
    }

    // TODO FolderController 따로 생성
    /** 폴더 관련 **/
    @GetMapping(value = "/folder")
    public ResponseEntity<List<FolderDto.Response>> getFolderName(@AuthenticationPrincipal UserDetailsImpl user){
        String userId = user.getUsername();
        List<FolderDto.Response> fdto =  folderService.getByFolderName(userId).stream()
                .map(FolderDto.Response::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(fdto);
    }

    @PostMapping (value = "/folder")
    public ResponseEntity<FolderDto.Response> setFolder(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody FolderDto.Request fdto){
        String userId = user.getUsername();
        fdto.setUser_id(userId);
        FolderDto.Response data = new FolderDto.Response(folderService.setByFolder(fdto));
        return ResponseEntity.ok(data);
    }

    /**
     * 폴더 데이터 변경
     * @param userId 로그인한 유저 아이디
     * @param id 폴더 아이디
     * @param fdto 폴더 이름, 폴더 색상, 글자 색상, 메모
     * @return 업데이트한 폴더 정보
     */
    @PutMapping (value = "/folder/{id}")
    public ResponseEntity<FolderDto.Response> updateFolder(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("id") int id, @RequestBody FolderDto.Request fdto){
        String userId = user.getUsername();
        fdto.setFolder_id(id);
        fdto.setUser_id(userId);
        FolderDto.Response data = new FolderDto.Response(folderService.updateByFolder(fdto));
        return ResponseEntity.ok(data);
    }

    @DeleteMapping(value="/folder/{id}")
    public ResponseEntity<Integer> removeFolder(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("id") int id){
        String userId = user.getUsername();
        FolderDto.Request fdto = new FolderDto.Request();
        fdto.setFolder_id(id);
        fdto.setUser_id(userId);
        int data = folderService.removeByFolder(fdto);
        return ResponseEntity.ok(data);
    }

}
