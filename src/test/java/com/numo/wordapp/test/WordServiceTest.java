package com.numo.wordapp.test;

import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.model.word.GttsCode;
import com.numo.wordapp.model.word.Word;
import com.numo.wordapp.model.word.detail.WordDetailMain;
import com.numo.wordapp.model.word.detail.WordDetailSub;
import com.numo.wordapp.model.word.detail.WordDetailTitle;
import com.numo.wordapp.repository.WordRepository;
import com.numo.wordapp.util.JsonUtil;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// jUnit 버전 문제로 실행 클래스 지정...
//지정 안하면 의존성 주입이 안됨
@RunWith(SpringRunner.class)
// 설정 파일 주입
@TestPropertySource(locations = "classpath:application-test.properties")
// 구동 환경 설정
@SpringBootTest(properties = "spring.profiles.active:test")
@Transactional
public class WordServiceTest {
    @Autowired
    WordRepository wordRepository;

    @DisplayName("단어 페이징 테스트")
    @Test
    public void testPageDefault(){
        Pageable pageable = PageRequest.of(0, 1);
        Page<Word> result = wordRepository.getByPageWord(pageable,"admin");
        //List <Word> result = wordRepository.getByAllWord("admin");
        List<WordDto.Response> res = result.getContent().stream().map(WordDto.Response::new).collect(Collectors.toList());

        String str = new JsonUtil().makeJson(res);
        System.out.println(str);
    }

    @Test
    public void testSaveWord(){
        Word word = Word.builder()
                .word("word")
                .userId("admin")
                .mean("mean")
                .read("read")
                .memo("memo")
                .lang(GttsCode.EN)
                .build();
        WordDetailMain wordDetailMain = WordDetailMain.builder()
                .word(word)
                .content("hello")
                .memo("memo")
                .build();
    }

    @Test
    public void updateTest(){
        Word word = wordRepository.findByUserIdAndWordId("admin", 325).orElseThrow();
        WordDetailSub wordDetailSub = WordDetailSub.builder()
                .detailSubId(word.getWordDetailMains().get((0)).getWordDetailSub().get(0).getDetailSubId())
                .wordDetailTitle(new WordDetailTitle(1))
                .content("sub데이터변경000")
                .build();
        WordDetailSub wordDetailSub2 = WordDetailSub.builder()
                .detailSubId(word.getWordDetailMains().get((0)).getWordDetailSub().get(1).getDetailSubId())
                .wordDetailTitle(new WordDetailTitle(1))
                .content("sub데이터변경111")
                .build();
        List<WordDetailSub> wordDetailSubs = new ArrayList<>();
        WordDetailMain wordDetailMain = WordDetailMain.builder()
                .word(word)
                .detailMainId(word.getWordDetailMains().get(0).getDetailMainId())
                .wordDetailTitle(new WordDetailTitle(2))
                .wordDetailSub(wordDetailSubs)
                .content("변경")
                .memo("memo")
                .build();
        word.getWordDetailMains().get(0).setUpdateTimeNow();
    }

    void print(Object obj){
        String str = new JsonUtil().makeJson(obj);
        System.out.println(str);
    }
}
