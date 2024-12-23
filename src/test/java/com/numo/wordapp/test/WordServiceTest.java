//package com.numo.wordapp.test;
//
//import com.numo.wordapp.comm.util.JsonUtil;
//import com.numo.wordapp.dto.page.PageDto;
//import com.numo.wordapp.dto.word.read.ReadWordRequestDto;
//import com.numo.wordapp.dto.word.WordResponseDto;
//import com.numo.wordapp.entity.word.type.GttsCode;
//import com.numo.wordapp.entity.word.Word;
//import com.numo.wordapp.entity.word.detail.WordDetailMain;
//import com.numo.wordapp.entity.word.detail.WordDetailSub;
//import com.numo.wordapp.entity.word.detail.WordDetailTitle;
//import com.numo.wordapp.repository.word.WordRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Slice;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//// 설정 파일 주입
//@TestPropertySource(locations = "classpath:application-test.properties")
//// 구동 환경 설정
//@SpringBootTest(properties = "spring.profiles.active:test")
//@Transactional
//public class WordServiceTest {
//    @Autowired
//    WordRepository wordRepository;
//
//    @DisplayName("단어 페이징 테스트")
//    @Test
//    public void testPageDefault(){
//        Pageable pageable = PageRequest.of(0, 20);
//        Long userId = 1L;
//        ReadWordRequestDto readDto = ReadWordRequestDto.builder()
//                .folderId(1L)
//                .build();
//        Slice<Word> result = wordRepository.findByPageWord(pageable, userId, readDto);
//        List<WordResponseDto> res = result.getContent().stream().map(WordResponseDto::new).collect(Collectors.toList());
//        PageDto pageRes = new PageDto(result);
//
//        String str = new JsonUtil().makeJson(res);
//        System.out.println("JSON:: \n"+str);
//
//        System.out.println("Page::"+new JsonUtil().makeJson(pageRes));
//    }
//
//    @Test
//    public void testSaveWord(){
//        Word word = Word.builder()
//                .word("word")
//                .userId("admin")
//                .mean("mean")
//                .read("read")
//                .memo("memo")
//                .lang(GttsCode.EN)
//                .build();
//        WordDetailMain wordDetailMain = WordDetailMain.builder()
//                .word(word)
//                .content("hello")
//                .memo("memo")
//                .build();
//    }
//
//    @Test
//    public void updateTest(){
//        Word word = wordRepository.findByIdAndWordId("admin", 325).orElseThrow();
//        WordDetailSub wordDetailSub = WordDetailSub.builder()
//                .detailSubId(word.getWordDetailMains().get((0)).getWordDetailSub().get(0).getDetailSubId())
//                .wordDetailTitle(new WordDetailTitle(1))
//                .content("sub데이터변경000")
//                .build();
//        WordDetailSub wordDetailSub2 = WordDetailSub.builder()
//                .detailSubId(word.getWordDetailMains().get((0)).getWordDetailSub().get(1).getDetailSubId())
//                .wordDetailTitle(new WordDetailTitle(1))
//                .content("sub데이터변경111")
//                .build();
//        List<WordDetailSub> wordDetailSubs = new ArrayList<>();
//        WordDetailMain wordDetailMain = WordDetailMain.builder()
//                .word(word)
//                .detailMainId(word.getWordDetailMains().get(0).getDetailMainId())
//                .wordDetailTitle(new WordDetailTitle(2))
//                .wordDetailSub(wordDetailSubs)
//                .content("변경")
//                .memo("memo")
//                .build();
//        word.getWordDetailMains().get(0).setUpdateTimeNow();
//    }
//
//    void print(Object obj){
//        String str = new JsonUtil().makeJson(obj);
//        System.out.println(str);
//    }
//
//}
